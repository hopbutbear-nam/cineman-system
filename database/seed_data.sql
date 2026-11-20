-- =====================================================================
-- CineMan - seed_data.sql  (chạy SAU schema.sql)
-- Mật khẩu của tất cả tài khoản mẫu: 123456  (BCrypt)
-- Ngày chiếu tính theo CURDATE() nên luôn có suất chiếu sắp tới.
-- Giá vé nằm ở loại ghế: Thường 70.000 | VIP 90.000 | Sweetbox 150.000 (VND).
-- =====================================================================
SET NAMES utf8mb4;
USE cineman;
SET @mk := '$2a$10$AVc6hiXacjohBlsZqNcqLOAm7E4RHd2VIASOBqWkOSeK.YuT1OE8S';

-- Rạp và phòng chiếu
INSERT INTO rap_chieu_phim (ten_rap, dia_chi, so_dien_thoai)
VALUES ('CineMan Hà Nội', '1 Đại Cồ Việt, Hai Bà Trưng, Hà Nội', '0241234567');
SET @rap := LAST_INSERT_ID();

INSERT INTO phong_chieu (rap_id, ten_phong, suc_chua, mo_ta) VALUES
  (@rap, 'P1', 32, 'Phòng 2D, 4 hàng x 8 ghế'),
  (@rap, 'P2', 50, 'Phòng 2D, 5 hàng x 10 ghế');

-- Loại ghế (giá vé được gán theo loại ghế): Thường, VIP, Sweetbox
INSERT INTO loai_ghe (ten_loai, don_gia, mo_ta) VALUES
  ('Thường',   70000, 'Ghế thường'),
  ('VIP',      90000, 'Ghế VIP trung tâm'),
  ('Sweetbox', 150000, 'Ghế đôi Sweetbox ở hàng cuối');

-- Ghế P1: hàng A-D, số 1-8 (A, B là Thường; C là VIP; D là Sweetbox)
INSERT INTO ghe (phong_chieu_id, loai_ghe_id, ma_ghe, hang, so_ghe)
SELECT (SELECT id FROM phong_chieu WHERE ten_phong = 'P1'),
       (SELECT id FROM loai_ghe WHERE ten_loai = CASE h.hang WHEN 'C' THEN 'VIP'
                                                              WHEN 'D' THEN 'Sweetbox'
                                                              ELSE 'Thường' END),
       CONCAT(h.hang, n.so), h.hang, n.so
FROM (SELECT 'A' AS hang UNION ALL SELECT 'B' UNION ALL SELECT 'C' UNION ALL SELECT 'D') h
CROSS JOIN (SELECT 1 AS so UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
            UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8) n;

-- Ghế P2: hàng A-E, số 1-10 (A, B là Thường; C, D là VIP; E là Sweetbox)
INSERT INTO ghe (phong_chieu_id, loai_ghe_id, ma_ghe, hang, so_ghe)
SELECT (SELECT id FROM phong_chieu WHERE ten_phong = 'P2'),
       (SELECT id FROM loai_ghe WHERE ten_loai = CASE
                                                   WHEN h.hang IN ('C', 'D') THEN 'VIP'
                                                   WHEN h.hang = 'E' THEN 'Sweetbox'
                                                   ELSE 'Thường' END),
       CONCAT(h.hang, n.so), h.hang, n.so
FROM (SELECT 'A' AS hang UNION ALL SELECT 'B' UNION ALL SELECT 'C' UNION ALL SELECT 'D' UNION ALL SELECT 'E') h
CROSS JOIN (SELECT 1 AS so UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 UNION ALL SELECT 5
            UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9 UNION ALL SELECT 10) n;

-- Người dùng
INSERT INTO nguoi_dung
  (loai_nguoi_dung, ten_dang_nhap, mat_khau, ho_ten, dia_chi, ngay_sinh, gioi_tinh, email, so_dien_thoai, chuc_vu, rap_id, trang_thai)
VALUES
  ('NV_BAN_HANG', 'nvbh01', @mk, 'Trần Thị Bán', 'Hà Nội', '1998-05-12', 'Nữ', 'nvbh01@cineman.vn', '0901000001', 'Nhân viên bán hàng', @rap, NULL),
  ('KHACH_HANG', 'khach01', @mk, 'Nguyễn Văn A', NULL, NULL, NULL, NULL, NULL, NULL, NULL, 'Chưa đăng ký'),
  ('KHACH_HANG', 'khach02', @mk, 'Lê Thị Bình', 'Hà Nội', '2001-03-20', 'Nữ', 'binh@gmail.com', '0911111111', NULL, NULL, 'Chưa đăng ký'),
  ('KHACH_HANG', 'khach03', @mk, 'Phạm Minh Châu', 'Hà Nội', '1995-09-09', 'Nam', 'chau@gmail.com', '0987654321', NULL, NULL, 'Đã đăng ký'),
  ('KHACH_HANG', 'khach04', @mk, 'Hoàng Thu Dung', 'Hà Nội', '1999-12-01', 'Nữ', 'dung@gmail.com', '0905555555', NULL, NULL, 'Đã đăng ký');

-- Thẻ thành viên (của khach03, khach04)
INSERT INTO the_thanh_vien (ma_the, khach_hang_id, nv_cap_id, ngay_cap, mo_ta)
SELECT 'CINE99281', k.id, (SELECT id FROM nguoi_dung WHERE ten_dang_nhap = 'nvbh01'), CURDATE() - INTERVAL 20 DAY, 'Thẻ thành viên'
FROM nguoi_dung k WHERE k.ten_dang_nhap = 'khach03';
INSERT INTO the_thanh_vien (ma_the, khach_hang_id, nv_cap_id, ngay_cap, mo_ta)
SELECT 'CINE10002', k.id, (SELECT id FROM nguoi_dung WHERE ten_dang_nhap = 'nvbh01'), CURDATE() - INTERVAL 10 DAY, 'Thẻ thành viên'
FROM nguoi_dung k WHERE k.ten_dang_nhap = 'khach04';

-- Phim
INSERT INTO phim (ten_phim, the_loai, dao_dien, dien_vien, thoi_luong, gioi_han_do_tuoi, ngay_khoi_chieu, ngay_ket_thuc, mo_ta, poster_url) VALUES
 ('Mai', 'Tâm lý, Tình cảm', 'Trấn Thành', 'Phương Anh Đào, Tuấn Trần', 131, 'T18', CURDATE() - INTERVAL 14 DAY, CURDATE() + INTERVAL 60 DAY, 'Câu chuyện về một người phụ nữ và cuộc đời nhiều biến động.', NULL),
 ('Mai An Tiêm', 'Hài hước, Gia đình', 'Nguyễn Quang Dũng', 'Quang Tuấn, Diệu Nhi', 90, 'T13', CURDATE() - INTERVAL 7 DAY, CURDATE() + INTERVAL 45 DAY, 'Phim hài gia đình dành cho mọi lứa tuổi.', NULL),
 ('Kung Fu Panda 4', 'Hoạt hình, Hành động', 'Mike Mitchell', 'Jack Black', 94, 'P', CURDATE() - INTERVAL 10 DAY, CURDATE() + INTERVAL 30 DAY, 'Gấu Po tiếp tục hành trình mới.', NULL),
 ('Đào, Phở và Piano', 'Chiến tranh, Lịch sử', 'Phi Tiến Sơn', 'Đỗ Hải Yến, Quang Thắng', 100, 'T13', CURDATE() - INTERVAL 5 DAY, CURDATE() + INTERVAL 40 DAY, 'Phim về Hà Nội mùa đông năm 1946.', NULL),
 ('Kẻ Đánh Cắp Giấc Mơ', 'Khoa học viễn tưởng', 'Đạo diễn mẫu', 'Diễn viên mẫu', 120, 'T16', CURDATE(), CURDATE() + INTERVAL 30 DAY, 'Phim mẫu CHƯA có suất chiếu (để thử ngoại lệ 6a).', NULL);

-- Suất chiếu: (phim, phòng, số ngày kể từ hôm nay, giờ bắt đầu). Giờ kết thúc = giờ bắt đầu + thời lượng phim.
-- Trigger của bảng suat_chieu kiểm tra: ngày chiếu nằm trong thời gian chiếu của phim,
-- giờ kết thúc đúng bằng giờ bắt đầu + thời lượng, không trùng suất khác trong cùng phòng.
INSERT INTO suat_chieu (phim_id, phong_chieu_id, ngay_chieu, gio_bat_dau, gio_ket_thuc)
SELECT p.id, pc.id, CURDATE() + INTERVAL s.off DAY, s.gio, ADDTIME(s.gio, SEC_TO_TIME(p.thoi_luong * 60))
FROM (
  SELECT 'Mai' ten_phim, 'P1' ten_phong, d.off, g.gio FROM
     (SELECT 0 AS off UNION ALL SELECT 1 UNION ALL SELECT 2) d
     CROSS JOIN (SELECT TIME('09:00:00') AS gio UNION ALL SELECT TIME('14:00:00') UNION ALL SELECT TIME('19:00:00')) g
  UNION ALL
  SELECT 'Mai An Tiêm', 'P2', d.off, g.gio FROM
     (SELECT 0 AS off UNION ALL SELECT 1 UNION ALL SELECT 2) d
     CROSS JOIN (SELECT TIME('09:30:00') AS gio UNION ALL SELECT TIME('14:30:00') UNION ALL SELECT TIME('19:30:00')) g
  UNION ALL
  SELECT 'Kung Fu Panda 4', 'P1', d.off, g.gio FROM
     (SELECT 0 AS off UNION ALL SELECT 1 UNION ALL SELECT 2) d
     CROSS JOIN (SELECT TIME('12:00:00') AS gio UNION ALL SELECT TIME('21:30:00')) g
  UNION ALL
  SELECT 'Đào, Phở và Piano', 'P2', d.off, g.gio FROM
     (SELECT 0 AS off UNION ALL SELECT 1 UNION ALL SELECT 2) d
     CROSS JOIN (SELECT TIME('12:00:00') AS gio UNION ALL SELECT TIME('22:00:00')) g
) s
JOIN phim p ON p.ten_phim = s.ten_phim
JOIN phong_chieu pc ON pc.ten_phong = s.ten_phong;

-- Bán sẵn vài vé để thử "ghế đã bán": phim Mai, P1, 19:00 ngày mai: B3, C5, C6
-- Tổng tiền = 70.000 (B3, ghế Thường) + 90.000 (C5, VIP) + 90.000 (C6, VIP) = 250.000
-- Hóa đơn tại quầy: bắt buộc có nhân viên lập; khách vãng lai nên khach_hang_id để trống.
INSERT INTO hoa_don (ngay_gio_lap, khach_hang_id, nv_ban_hang_id, loai, phuong_thuc_thanh_toan, tong_tien)
VALUES (NOW(), NULL, (SELECT id FROM nguoi_dung WHERE ten_dang_nhap = 'nvbh01'), 'Tại quầy', 'Tiền mặt', 250000);
SET @hd := LAST_INSERT_ID();

-- Mã vé: yyyyMMdd-HHmm-M<phim_id>-S<suat_chieu_id>-<ma_ghe>  (không liên quan tới ve.id)
INSERT INTO ve (ma_ve, hoa_don_id, suat_chieu_id, ghe_id)
SELECT CONCAT(DATE_FORMAT(sc.ngay_chieu, '%Y%m%d'), '-', TIME_FORMAT(sc.gio_bat_dau, '%H%i'),
              '-M', sc.phim_id, '-S', sc.id, '-', g.ma_ghe),
       @hd, sc.id, g.id
FROM suat_chieu sc
JOIN phim p ON p.id = sc.phim_id AND p.ten_phim = 'Mai'
JOIN ghe g ON g.phong_chieu_id = sc.phong_chieu_id
WHERE sc.ngay_chieu = CURDATE() + INTERVAL 1 DAY
  AND sc.gio_bat_dau = '19:00:00'
  AND g.ma_ghe IN ('B3', 'C5', 'C6');
