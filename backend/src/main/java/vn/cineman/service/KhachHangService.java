package vn.cineman.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.cineman.dto.DangKyThanhVienRequest;
import vn.cineman.dto.DangKyThanhVienResponse;
import vn.cineman.dto.ThanhVienDto;
import vn.cineman.entity.KhachHang;
import vn.cineman.entity.TheThanhVien;
import vn.cineman.exception.ApiException;
import vn.cineman.repository.KhachHangRepository;
import vn.cineman.repository.NguoiDungRepository;
import vn.cineman.repository.TheThanhVienRepository;

/** Nghiep vu 1: Khach hang dang ky thanh vien. */
@Service
public class KhachHangService {
    private static final String THONG_BAO_THANH_CONG =
            "Đăng ký thành viên thành công! Quý khách vui lòng tới nhận thẻ thành viên trực tiếp tại quầy lễ tân.";

    private final KhachHangRepository khachHangRepo;
    private final NguoiDungRepository nguoiDungRepo;
    private final TheThanhVienRepository theRepo;

    public KhachHangService(KhachHangRepository khachHangRepo, NguoiDungRepository nguoiDungRepo,
                            TheThanhVienRepository theRepo) {
        this.khachHangRepo = khachHangRepo;
        this.nguoiDungRepo = nguoiDungRepo;
        this.theRepo = theRepo;
    }

    @Transactional(readOnly = true)
    public ThanhVienDto layTrangThaiThanhVien(Long khachHangId) {
        return toDto(timKhachHang(khachHangId));
    }

    @Transactional
    public DangKyThanhVienResponse dangKyThanhVien(Long khachHangId, DangKyThanhVienRequest req) {
        KhachHang kh = timKhachHang(khachHangId);

        // Rang buoc 1: chua dang ky thanh vien
        if (KhachHang.DA_DANG_KY.equals(kh.getTrangThai())) {
            throw ApiException.conflict("Bạn đã đăng ký thành viên rồi, không thể đăng ký lại");
        }
        // Rang buoc 2: so dien thoai chua bi nguoi khac su dung
        String sdt = req.soDienThoai().trim();
        if (nguoiDungRepo.existsBySoDienThoaiAndIdNot(sdt, kh.getId())) {
            throw ApiException.loiTruong(HttpStatus.CONFLICT, "soDienThoai",
                    "Số điện thoại đã được thành viên khác sử dụng, vui lòng nhập số khác");
        }
        if (req.ngaySinh().getYear() < 1900) {
            throw ApiException.loiTruong(HttpStatus.BAD_REQUEST, "ngaySinh", "Ngày sinh không hợp lệ");
        }

        // Cap nhat thong tin ca nhan + chuyen trang thai thanh vien
        kh.setHoTen(req.hoTen().trim());
        kh.setSoDienThoai(sdt);
        kh.setEmail(req.email().trim());
        kh.setGioiTinh(req.gioiTinh());
        kh.setNgaySinh(req.ngaySinh());
        kh.setTrangThai(KhachHang.DA_DANG_KY);
        try {
            khachHangRepo.saveAndFlush(kh);
        } catch (DataIntegrityViolationException e) { // hai yeu cau trung SDT cung luc
            throw ApiException.loiTruong(HttpStatus.CONFLICT, "soDienThoai",
                    "Số điện thoại đã được thành viên khác sử dụng, vui lòng nhập số khác");
        }
        return new DangKyThanhVienResponse(THONG_BAO_THANH_CONG, toDto(kh));
    }

    private ThanhVienDto toDto(KhachHang kh) {
        String maThe = theRepo.findByKhachHangId(kh.getId()).map(TheThanhVien::getMaThe).orElse(null);
        return new ThanhVienDto(kh.getTrangThai(), maThe, kh.getHoTen(), kh.getSoDienThoai(), kh.getEmail(),
                kh.getGioiTinh(), kh.getNgaySinh());
    }

    private KhachHang timKhachHang(Long id) {
        return khachHangRepo.findById(id).orElseThrow(() -> ApiException.notFound("Không tìm thấy khách hàng"));
    }
}
