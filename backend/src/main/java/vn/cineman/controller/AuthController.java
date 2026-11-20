package vn.cineman.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.cineman.dto.CapNhatThongTinRequest;
import vn.cineman.dto.DoiMatKhauRequest;
import vn.cineman.dto.LoginRequest;
import vn.cineman.dto.LoginResponse;
import vn.cineman.dto.NguoiDungDto;
import vn.cineman.security.PhienDangNhap;
import vn.cineman.service.AuthService;

/** Chuc nang chung cua moi nguoi dung: dang nhap, dang xuat, doi mat khau, cap nhat thong tin. */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public LoginResponse dangNhap(@Valid @RequestBody LoginRequest req) {
        return authService.dangNhap(req);
    }

    @PostMapping("/logout")
    public void dangXuat(PhienDangNhap phien) {
        authService.dangXuat(phien);
    }

    @GetMapping("/me")
    public NguoiDungDto thongTinCaNhan(PhienDangNhap phien) {
        return authService.layThongTin(phien.nguoiDungId());
    }

    @PutMapping("/me")
    public NguoiDungDto capNhatThongTin(PhienDangNhap phien, @Valid @RequestBody CapNhatThongTinRequest req) {
        return authService.capNhatThongTin(phien.nguoiDungId(), req);
    }

    @PutMapping("/mat-khau")
    public void doiMatKhau(PhienDangNhap phien, @Valid @RequestBody DoiMatKhauRequest req) {
        authService.doiMatKhau(phien, req);
    }
}
