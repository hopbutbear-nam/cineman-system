package vn.cineman.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.cineman.dto.DangKyThanhVienRequest;
import vn.cineman.dto.DangKyThanhVienResponse;
import vn.cineman.dto.ThanhVienDto;
import vn.cineman.security.PhienDangNhap;
import vn.cineman.security.VaiTro;
import vn.cineman.security.YeuCauVaiTro;
import vn.cineman.service.KhachHangService;

/** Nghiep vu 1 - tac nhan KhachHang. */
@RestController
@RequestMapping("/api/khach-hang")
@YeuCauVaiTro(VaiTro.KHACH_HANG)
public class KhachHangController {
    private final KhachHangService khachHangService;

    public KhachHangController(KhachHangService khachHangService) {
        this.khachHangService = khachHangService;
    }

    /** Lay trang thai thanh vien + thong tin hien co de dien san form. */
    @GetMapping("/thanh-vien")
    public ThanhVienDto trangThaiThanhVien(PhienDangNhap phien) {
        return khachHangService.layTrangThaiThanhVien(phien.nguoiDungId());
    }

    @PostMapping("/thanh-vien")
    public DangKyThanhVienResponse dangKyThanhVien(PhienDangNhap phien,
                                                   @Valid @RequestBody DangKyThanhVienRequest req) {
        return khachHangService.dangKyThanhVien(phien.nguoiDungId(), req);
    }
}
