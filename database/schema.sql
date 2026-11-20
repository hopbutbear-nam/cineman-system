-- =====================================================================
-- CineMan - schema.sql  (MySQL 8.0.16 trở lên, vì dùng ràng buộc CHECK)
-- Phạm vi: Đăng ký thành viên + Bán vé tại quầy
--          (+ đăng nhập / đổi mật khẩu / cập nhật hồ sơ).
-- Chạy bằng MySQL Workbench: File > Open SQL Script > chọn file > Execute.
-- CẢNH BÁO: script xóa và tạo lại cơ sở dữ liệu `cineman`.
--
-- Các quy tắc nghiệp vụ được đảm bảo ngay trong CSDL:
--   * Giá vé chỉ nằm ở loại ghế (loai_ghe.don_gia); bảng `ve` không lưu giá.
--   * Loại ghế chỉ có 3 loại: Thường, VIP, Sweetbox.
--   * Mã vé (ve.ma_ve) xác định ngày xem, suất chiếu, ghế và phim;
--     không liên quan tới `ve.id` (khóa chính dùng để phân biệt từng vé).
--   * Suất chiếu và ghế của một vé phải thuộc cùng một phòng chiếu.
--   * Một ghế chỉ bán một lần cho mỗi suất chiếu.
--   * Các suất chiếu trong cùng một phòng không được trùng thời gian.
--   * Hóa đơn tại quầy bắt buộc có nhân viên lập; hóa đơn trực tuyến bắt buộc
--     có khách hàng và có phương thức thanh toán "Thanh toán trực tuyến".
-- =====================================================================
SET NAMES utf8mb4;

DROP DATABASE IF EXISTS cineman;
CREATE DATABASE cineman CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE cineman;

-- ---------- Cơ sở vật chất ----------
CREATE TABLE rap_chieu_phim (
    id             BIGINT       NOT NULL AUTO_INCREMENT,
    ten_rap        VARCHAR(150) NOT NULL,
    dia_chi        VARCHAR(255),
    so_dien_thoai  VARCHAR(15),
    PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE phong_chieu (
    id         BIGINT       NOT NULL AUTO_INCREMENT,
    rap_id     BIGINT       NOT NULL,
    ten_phong  VARCHAR(50)  NOT NULL,
    suc_chua   INT          NOT NULL,                 -- số ghế tối đa của phòng
    mo_ta      VARCHAR(255),
    PRIMARY KEY (id),
    UNIQUE KEY uk_phong_rap_ten (rap_id, ten_phong),
    CONSTRAINT fk_phong_rap FOREIGN KEY (rap_id) REFERENCES rap_chieu_phim (id),
    CONSTRAINT chk_phong_suc_chua CHECK (suc_chua > 0)
) ENGINE=InnoDB;

-- Loại ghế: giá vé được gán vào loại ghế (ghế thuộc loại nào thì bán theo giá của loại đó).
CREATE TABLE loai_ghe (
    id        BIGINT       NOT NULL AUTO_INCREMENT,
    ten_loai  VARCHAR(50)  NOT NULL,
    don_gia   BIGINT       NOT NULL,                  -- VND, giá vé theo loại ghế
    mo_ta     VARCHAR(255),
    PRIMARY KEY (id),
    UNIQUE KEY uk_loai_ghe_ten (ten_loai),
    CONSTRAINT chk_loai_ghe_ten CHECK (ten_loai IN ('Thường', 'VIP', 'Sweetbox')),
    CONSTRAINT chk_loai_ghe_gia CHECK (don_gia > 0)
) ENGINE=InnoDB;

CREATE TABLE ghe (
    id              BIGINT      NOT NULL AUTO_INCREMENT,
    phong_chieu_id  BIGINT      NOT NULL,
    loai_ghe_id     BIGINT      NOT NULL,
    ma_ghe          VARCHAR(10) NOT NULL,             -- ví dụ: C3
    hang            VARCHAR(2)  NOT NULL,             -- ví dụ: C
    so_ghe          INT         NOT NULL,             -- ví dụ: 3
    mo_ta           VARCHAR(255),
    PRIMARY KEY (id),
    UNIQUE KEY uk_ghe_phong_ma (phong_chieu_id, ma_ghe),
    UNIQUE KEY uk_ghe_phong_vitri (phong_chieu_id, hang, so_ghe),
    CONSTRAINT fk_ghe_phong FOREIGN KEY (phong_chieu_id) REFERENCES phong_chieu (id),
    CONSTRAINT fk_ghe_loai  FOREIGN KEY (loai_ghe_id)    REFERENCES loai_ghe (id),
    CONSTRAINT chk_ghe_so  CHECK (so_ghe > 0),
    CONSTRAINT chk_ghe_ma  CHECK (ma_ghe = CONCAT(hang, so_ghe))   -- mã ghế = hàng + số
) ENGINE=InnoDB;

-- ---------- Người dùng (một bảng chung: KHACH_HANG | NV_BAN_HANG) ----------
CREATE TABLE nguoi_dung (
    id               BIGINT       NOT NULL AUTO_INCREMENT,
    loai_nguoi_dung  VARCHAR(20)  NOT NULL,           -- cột phân loại của JPA
    ten_dang_nhap    VARCHAR(50)  NOT NULL,
    mat_khau         VARCHAR(100) NOT NULL,           -- BCrypt
    ho_ten           VARCHAR(100) NOT NULL,
    dia_chi          VARCHAR(255),
    ngay_sinh        DATE,
    gioi_tinh        VARCHAR(10),
    email            VARCHAR(100),
    so_dien_thoai    VARCHAR(15),
    chuc_vu          VARCHAR(50),                     -- chỉ dùng cho nhân viên
    rap_id           BIGINT,                          -- chỉ dùng cho nhân viên
    trang_thai       VARCHAR(20),                     -- trạng thái thành viên, chỉ dùng cho khách hàng
    PRIMARY KEY (id),
    UNIQUE KEY uk_nd_ten_dang_nhap (ten_dang_nhap),
    UNIQUE KEY uk_nd_so_dien_thoai (so_dien_thoai),   -- NULL được lặp, giá trị thật phải duy nhất
    CONSTRAINT chk_nd_loai CHECK (loai_nguoi_dung IN ('KHACH_HANG', 'NV_BAN_HANG')),
    CONSTRAINT chk_nd_gioi_tinh CHECK (gioi_tinh IS NULL OR gioi_tinh IN ('Nam', 'Nữ', 'Khác')),
    CONSTRAINT chk_nd_trang_thai CHECK (trang_thai IS NULL OR trang_thai IN ('Chưa đăng ký', 'Đã đăng ký')),
    -- Khách hàng đã đăng ký thành viên thì hồ sơ phải đầy đủ: số điện thoại, email, giới tính, ngày sinh.
    CONSTRAINT chk_nd_thanh_vien_day_du CHECK (
        trang_thai IS NULL OR trang_thai <> 'Đã đăng ký'
        OR (so_dien_thoai IS NOT NULL AND email IS NOT NULL AND gioi_tinh IS NOT NULL AND ngay_sinh IS NOT NULL)
    ),
    CONSTRAINT fk_nd_rap FOREIGN KEY (rap_id) REFERENCES rap_chieu_phim (id)
) ENGINE=InnoDB;

-- Thẻ thành viên: cấp tại quầy cho khách hàng đã đăng ký (trigger ở cuối file kiểm tra).
CREATE TABLE the_thanh_vien (
    id             BIGINT      NOT NULL AUTO_INCREMENT,
    ma_the         VARCHAR(20) NOT NULL,
    khach_hang_id  BIGINT      NOT NULL,
    nv_cap_id      BIGINT,
    ngay_cap       DATE        NOT NULL,
    mo_ta          VARCHAR(255),
    PRIMARY KEY (id),
    UNIQUE KEY uk_the_ma (ma_the),
    UNIQUE KEY uk_the_khach_hang (khach_hang_id),     -- mỗi khách hàng tối đa 1 thẻ
    CONSTRAINT fk_the_khach_hang FOREIGN KEY (khach_hang_id) REFERENCES nguoi_dung (id),
    CONSTRAINT fk_the_nv_cap     FOREIGN KEY (nv_cap_id)     REFERENCES nguoi_dung (id)
) ENGINE=InnoDB;

-- ---------- Phim và suất chiếu ----------
CREATE TABLE phim (
    id                BIGINT       NOT NULL AUTO_INCREMENT,
    ten_phim          VARCHAR(200) NOT NULL,
    the_loai          VARCHAR(100),
    dao_dien          VARCHAR(100),
    dien_vien         VARCHAR(255),
    thoi_luong        INT          NOT NULL,          -- phút
    gioi_han_do_tuoi  VARCHAR(10),
    ngay_khoi_chieu   DATE         NOT NULL,
    ngay_ket_thuc     DATE         NOT NULL,
    mo_ta             TEXT,
    poster_url        VARCHAR(500),
    PRIMARY KEY (id),
    INDEX idx_phim_ten (ten_phim),
    CONSTRAINT chk_phim_thoi_luong CHECK (thoi_luong > 0),
    CONSTRAINT chk_phim_ngay CHECK (ngay_ket_thuc >= ngay_khoi_chieu)
) ENGINE=InnoDB;

CREATE TABLE suat_chieu (
    id              BIGINT NOT NULL AUTO_INCREMENT,
    phim_id         BIGINT NOT NULL,
    phong_chieu_id  BIGINT NOT NULL,
    ngay_chieu      DATE   NOT NULL,
    gio_bat_dau     TIME   NOT NULL,
    gio_ket_thuc    TIME   NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_suat_phong_ngay_gio (phong_chieu_id, ngay_chieu, gio_bat_dau),
    INDEX idx_suat_phim_ngay (phim_id, ngay_chieu),
    CONSTRAINT fk_suat_phim  FOREIGN KEY (phim_id)        REFERENCES phim (id),
    CONSTRAINT fk_suat_phong FOREIGN KEY (phong_chieu_id) REFERENCES phong_chieu (id),
    CONSTRAINT chk_suat_gio CHECK (gio_ket_thuc > gio_bat_dau),
    -- Suất chiếu phải kết thúc trước 23 giờ 59 phút của chính ngày chiếu (không qua nửa đêm).
    CONSTRAINT chk_suat_trong_ngay CHECK (gio_ket_thuc <= TIME '23:59:00')
) ENGINE=InnoDB;
-- Các quy tắc còn lại của suất chiếu (cần đọc bảng khác) nằm ở trigger cuối file:
--   * ngày chiếu nằm trong khoảng [ngày khởi chiếu, ngày kết thúc] của phim;
--   * giờ kết thúc = giờ bắt đầu + thời lượng phim;
--   * không trùng thời gian với suất chiếu khác trong cùng phòng.

-- ---------- Hóa đơn và vé ----------
CREATE TABLE hoa_don (
    id                      BIGINT       NOT NULL AUTO_INCREMENT,
    ngay_gio_lap            DATETIME     NOT NULL,
    khach_hang_id           BIGINT       NULL,        -- NULL: khách vãng lai (chỉ được phép với hóa đơn tại quầy)
    nv_ban_hang_id          BIGINT       NULL,        -- bắt buộc khi bán tại quầy; NULL khi mua trực tuyến
    loai                    VARCHAR(20)  NOT NULL,    -- 'Tại quầy' | 'Trực tuyến'
    phuong_thuc_thanh_toan  VARCHAR(40)  NOT NULL,    -- 'Tiền mặt' | 'Chuyển khoản ngân hàng' | 'Thanh toán trực tuyến'
    tong_tien               BIGINT       NOT NULL,    -- tổng giá của các ghế (theo loại ghế) tại thời điểm lập hóa đơn
    PRIMARY KEY (id),
    INDEX idx_hd_khach_hang (khach_hang_id),
    INDEX idx_hd_nv (nv_ban_hang_id),
    CONSTRAINT fk_hd_khach_hang FOREIGN KEY (khach_hang_id)  REFERENCES nguoi_dung (id),
    CONSTRAINT fk_hd_nv         FOREIGN KEY (nv_ban_hang_id) REFERENCES nguoi_dung (id),
    CONSTRAINT chk_hd_loai CHECK (loai IN ('Tại quầy', 'Trực tuyến')),
    CONSTRAINT chk_hd_phuong_thuc CHECK (
        phuong_thuc_thanh_toan IN ('Tiền mặt', 'Chuyển khoản ngân hàng', 'Thanh toán trực tuyến')
    ),
    CONSTRAINT chk_hd_tong CHECK (tong_tien >= 0),
    -- Hóa đơn tại quầy: bắt buộc có nhân viên lập; thanh toán bằng tiền mặt hoặc chuyển khoản;
    -- khách hàng có thể để trống (khách vãng lai).
    CONSTRAINT chk_hd_tai_quay CHECK (
        loai <> 'Tại quầy'
        OR (nv_ban_hang_id IS NOT NULL AND phuong_thuc_thanh_toan IN ('Tiền mặt', 'Chuyển khoản ngân hàng'))
    ),
    -- Hóa đơn trực tuyến: bắt buộc có khách hàng, không có nhân viên lập,
    -- và phương thức thanh toán luôn là "Thanh toán trực tuyến".
    CONSTRAINT chk_hd_truc_tuyen CHECK (
        loai <> 'Trực tuyến'
        OR (khach_hang_id IS NOT NULL AND nv_ban_hang_id IS NULL AND phuong_thuc_thanh_toan = 'Thanh toán trực tuyến')
    )
) ENGINE=InnoDB;

-- Vé = (suất chiếu, ghế). Giá vé không lưu ở đây mà lấy từ loại ghế của ghế (ghe -> loai_ghe.don_gia).
CREATE TABLE ve (
    id              BIGINT      NOT NULL AUTO_INCREMENT,      -- phân biệt riêng từng vé
    ma_ve           VARCHAR(40) NOT NULL,                     -- xác định ngày xem, suất chiếu, ghế, phim
    hoa_don_id      BIGINT      NOT NULL,
    suat_chieu_id   BIGINT      NOT NULL,
    ghe_id          BIGINT      NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_ve_ma (ma_ve),
    UNIQUE KEY uk_ve_suat_ghe (suat_chieu_id, ghe_id),        -- 1 ghế chỉ bán 1 lần / suất chiếu
    INDEX idx_ve_hoa_don (hoa_don_id),
    INDEX idx_ve_ghe (ghe_id),
    CONSTRAINT fk_ve_hoa_don FOREIGN KEY (hoa_don_id)    REFERENCES hoa_don (id),
    CONSTRAINT fk_ve_suat    FOREIGN KEY (suat_chieu_id) REFERENCES suat_chieu (id),
    CONSTRAINT fk_ve_ghe     FOREIGN KEY (ghe_id)        REFERENCES ghe (id)
) ENGINE=InnoDB;
-- Định dạng mã vé:  yyyyMMdd-HHmm-M<phim_id>-S<suat_chieu_id>-<ma_ghe>
--   ví dụ: 20261121-1900-M1-S7-C5  (phim 1, suất 7, ghế C5, chiếu 19:00 ngày 21/11/2026)
-- Mã vé do ứng dụng sinh ra; trigger bên dưới kiểm tra mã vé khớp với suất chiếu và ghế.

-- =====================================================================
-- TRIGGER: các ràng buộc cần đọc dữ liệu của bảng khác (CHECK không làm được)
-- =====================================================================
DELIMITER $$

-- ---------- Suất chiếu ----------
CREATE PROCEDURE sp_kiem_tra_suat_chieu(
    IN p_id        BIGINT,       -- NULL hoặc 0 khi thêm mới
    IN p_phim_id   BIGINT,
    IN p_phong_id  BIGINT,
    IN p_ngay      DATE,
    IN p_bat_dau   TIME,
    IN p_ket_thuc  TIME
)
BEGIN
    DECLARE v_thoi_luong  INT;
    DECLARE v_khoi_chieu  DATE;
    DECLARE v_ket_thuc    DATE;

    SELECT thoi_luong, ngay_khoi_chieu, ngay_ket_thuc
      INTO v_thoi_luong, v_khoi_chieu, v_ket_thuc
      FROM phim WHERE id = p_phim_id;

    -- Ngày chiếu phải nằm giữa ngày khởi chiếu và ngày kết thúc của phim
    IF p_ngay < v_khoi_chieu OR p_ngay > v_ket_thuc THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Ngày chiếu phải nằm trong khoảng từ ngày khởi chiếu đến ngày kết thúc của phim';
    END IF;

    -- Suất chiếu phải kết thúc trước 23:59 của ngày chiếu
    IF TIME_TO_SEC(p_bat_dau) + v_thoi_luong * 60 > 23 * 3600 + 59 * 60 THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Suất chiếu phải kết thúc trước 23 giờ 59 phút của ngày chiếu';
    END IF;

    -- Giờ kết thúc = giờ bắt đầu + thời lượng phim
    IF p_ket_thuc <> ADDTIME(p_bat_dau, SEC_TO_TIME(v_thoi_luong * 60)) THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Giờ kết thúc phải bằng giờ bắt đầu cộng thời lượng phim';
    END IF;

    -- Các suất chiếu trong cùng một phòng không được trùng thời gian
    IF EXISTS (SELECT 1 FROM suat_chieu
                WHERE phong_chieu_id = p_phong_id
                  AND ngay_chieu = p_ngay
                  AND id <> IFNULL(p_id, 0)
                  AND gio_bat_dau < p_ket_thuc
                  AND gio_ket_thuc > p_bat_dau) THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Suất chiếu bị trùng thời gian với một suất chiếu khác trong cùng phòng';
    END IF;
END$$

CREATE TRIGGER trg_suat_chieu_bi BEFORE INSERT ON suat_chieu
FOR EACH ROW
BEGIN
    CALL sp_kiem_tra_suat_chieu(NEW.id, NEW.phim_id, NEW.phong_chieu_id, NEW.ngay_chieu,
                                NEW.gio_bat_dau, NEW.gio_ket_thuc);
END$$

CREATE TRIGGER trg_suat_chieu_bu BEFORE UPDATE ON suat_chieu
FOR EACH ROW
BEGIN
    CALL sp_kiem_tra_suat_chieu(NEW.id, NEW.phim_id, NEW.phong_chieu_id, NEW.ngay_chieu,
                                NEW.gio_bat_dau, NEW.gio_ket_thuc);
END$$

-- ---------- Vé: suất chiếu - ghế - mã vé ----------
CREATE PROCEDURE sp_kiem_tra_ve(
    IN p_suat_chieu_id  BIGINT,
    IN p_ghe_id         BIGINT,
    IN p_ma_ve          VARCHAR(40)
)
BEGIN
    DECLARE v_phong_suat  BIGINT;
    DECLARE v_phim_id     BIGINT;
    DECLARE v_ngay        DATE;
    DECLARE v_gio         TIME;
    DECLARE v_phong_ghe   BIGINT;
    DECLARE v_ma_ghe      VARCHAR(10);

    SELECT phong_chieu_id, phim_id, ngay_chieu, gio_bat_dau
      INTO v_phong_suat, v_phim_id, v_ngay, v_gio
      FROM suat_chieu WHERE id = p_suat_chieu_id;

    SELECT phong_chieu_id, ma_ghe
      INTO v_phong_ghe, v_ma_ghe
      FROM ghe WHERE id = p_ghe_id;

    -- Suất chiếu và ghế phải cùng một phòng chiếu
    IF v_phong_suat <> v_phong_ghe THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Ghế không thuộc phòng chiếu của suất chiếu';
    END IF;

    -- Mã vé phải khớp với ngày xem, suất chiếu, ghế và phim
    IF BINARY p_ma_ve <> BINARY CONCAT(DATE_FORMAT(v_ngay, '%Y%m%d'), '-', TIME_FORMAT(v_gio, '%H%i'),
                                       '-M', v_phim_id, '-S', p_suat_chieu_id, '-', v_ma_ghe) THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Mã vé không khớp với ngày xem, suất chiếu, ghế và phim';
    END IF;
END$$

CREATE TRIGGER trg_ve_bi BEFORE INSERT ON ve
FOR EACH ROW
BEGIN
    CALL sp_kiem_tra_ve(NEW.suat_chieu_id, NEW.ghe_id, NEW.ma_ve);
END$$

CREATE TRIGGER trg_ve_bu BEFORE UPDATE ON ve
FOR EACH ROW
BEGIN
    CALL sp_kiem_tra_ve(NEW.suat_chieu_id, NEW.ghe_id, NEW.ma_ve);
END$$

-- ---------- Hóa đơn: đúng vai trò người dùng ----------
CREATE PROCEDURE sp_kiem_tra_hoa_don(
    IN p_khach_hang_id   BIGINT,
    IN p_nv_ban_hang_id  BIGINT
)
BEGIN
    IF p_khach_hang_id IS NOT NULL AND NOT EXISTS
       (SELECT 1 FROM nguoi_dung WHERE id = p_khach_hang_id AND loai_nguoi_dung = 'KHACH_HANG') THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Người mua trên hóa đơn phải là khách hàng';
    END IF;

    IF p_nv_ban_hang_id IS NOT NULL AND NOT EXISTS
       (SELECT 1 FROM nguoi_dung WHERE id = p_nv_ban_hang_id AND loai_nguoi_dung = 'NV_BAN_HANG') THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Người lập hóa đơn phải là nhân viên bán hàng';
    END IF;
END$$

CREATE TRIGGER trg_hoa_don_bi BEFORE INSERT ON hoa_don
FOR EACH ROW
BEGIN
    CALL sp_kiem_tra_hoa_don(NEW.khach_hang_id, NEW.nv_ban_hang_id);
END$$

CREATE TRIGGER trg_hoa_don_bu BEFORE UPDATE ON hoa_don
FOR EACH ROW
BEGIN
    CALL sp_kiem_tra_hoa_don(NEW.khach_hang_id, NEW.nv_ban_hang_id);
END$$

-- ---------- Thẻ thành viên: chỉ cấp cho khách hàng đã đăng ký, do nhân viên bán hàng cấp ----------
CREATE PROCEDURE sp_kiem_tra_the(
    IN p_khach_hang_id  BIGINT,
    IN p_nv_cap_id      BIGINT
)
BEGIN
    IF NOT EXISTS (SELECT 1 FROM nguoi_dung
                    WHERE id = p_khach_hang_id AND loai_nguoi_dung = 'KHACH_HANG'
                      AND trang_thai = 'Đã đăng ký') THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Chỉ cấp thẻ thành viên cho khách hàng đã đăng ký thành viên';
    END IF;

    IF p_nv_cap_id IS NOT NULL AND NOT EXISTS
       (SELECT 1 FROM nguoi_dung WHERE id = p_nv_cap_id AND loai_nguoi_dung = 'NV_BAN_HANG') THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Người cấp thẻ phải là nhân viên bán hàng';
    END IF;
END$$

CREATE TRIGGER trg_the_bi BEFORE INSERT ON the_thanh_vien
FOR EACH ROW
BEGIN
    CALL sp_kiem_tra_the(NEW.khach_hang_id, NEW.nv_cap_id);
END$$

CREATE TRIGGER trg_the_bu BEFORE UPDATE ON the_thanh_vien
FOR EACH ROW
BEGIN
    CALL sp_kiem_tra_the(NEW.khach_hang_id, NEW.nv_cap_id);
END$$

DELIMITER ;
