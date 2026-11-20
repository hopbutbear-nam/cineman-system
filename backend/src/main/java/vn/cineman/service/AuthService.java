package vn.cineman.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.cineman.dto.CapNhatThongTinRequest;
import vn.cineman.dto.DoiMatKhauRequest;
import vn.cineman.dto.LoginRequest;
import vn.cineman.dto.LoginResponse;
import vn.cineman.dto.NguoiDungDto;
import vn.cineman.entity.NguoiDung;
import vn.cineman.exception.ApiException;
import vn.cineman.repository.NguoiDungRepository;
import vn.cineman.security.PhienDangNhap;
import vn.cineman.security.TokenStore;

/** Dang nhap / dang xuat / doi mat khau / cap nhat thong tin ca nhan. */
@Service
public class AuthService {
    private final NguoiDungRepository nguoiDungRepo;
    private final PasswordEncoder passwordEncoder;
    private final TokenStore tokenStore;

    public AuthService(NguoiDungRepository nguoiDungRepo, PasswordEncoder passwordEncoder, TokenStore tokenStore) {
        this.nguoiDungRepo = nguoiDungRepo;
        this.passwordEncoder = passwordEncoder;
        this.tokenStore = tokenStore;
    }

    @Transactional(readOnly = true)
    public LoginResponse dangNhap(LoginRequest req) {
        NguoiDung nd = nguoiDungRepo.findByTenDangNhap(req.tenDangNhap().trim())
                .filter(u -> passwordEncoder.matches(req.matKhau(), u.getMatKhau()))
                .orElseThrow(() -> ApiException.unauthorized("Tên đăng nhập hoặc mật khẩu không đúng"));
        PhienDangNhap phien = tokenStore.taoPhien(nd.getId(), nd.getVaiTro());
        return new LoginResponse(phien.token(), DtoMapper.toDto(nd));
    }

    public void dangXuat(PhienDangNhap phien) {
        tokenStore.huy(phien.token());
    }

    @Transactional(readOnly = true)
    public NguoiDungDto layThongTin(Long nguoiDungId) {
        return DtoMapper.toDto(timNguoiDung(nguoiDungId));
    }

    @Transactional
    public NguoiDungDto capNhatThongTin(Long nguoiDungId, CapNhatThongTinRequest req) {
        NguoiDung nd = timNguoiDung(nguoiDungId);
        String sdt = rongThanhNull(req.soDienThoai());
        if (sdt != null && nguoiDungRepo.existsBySoDienThoaiAndIdNot(sdt, nguoiDungId)) {
            throw ApiException.loiTruong(HttpStatus.CONFLICT, "soDienThoai", "Số điện thoại đã được sử dụng");
        }
        nd.setHoTen(req.hoTen().trim());
        nd.setDiaChi(rongThanhNull(req.diaChi()));
        nd.setNgaySinh(req.ngaySinh());
        nd.setGioiTinh(rongThanhNull(req.gioiTinh()));
        nd.setEmail(rongThanhNull(req.email()));
        nd.setSoDienThoai(sdt);
        return DtoMapper.toDto(nguoiDungRepo.save(nd));
    }

    @Transactional
    public void doiMatKhau(PhienDangNhap phien, DoiMatKhauRequest req) {
        NguoiDung nd = timNguoiDung(phien.nguoiDungId());
        if (!passwordEncoder.matches(req.matKhauHienTai(), nd.getMatKhau())) {
            throw ApiException.loiTruong(HttpStatus.BAD_REQUEST, "matKhauHienTai", "Mật khẩu hiện tại không đúng");
        }
        if (passwordEncoder.matches(req.matKhauMoi(), nd.getMatKhau())) {
            throw ApiException.loiTruong(HttpStatus.BAD_REQUEST, "matKhauMoi", "Mật khẩu mới phải khác mật khẩu hiện tại");
        }
        nd.setMatKhau(passwordEncoder.encode(req.matKhauMoi()));
        nguoiDungRepo.save(nd);
        tokenStore.huyCacPhienKhac(nd.getId(), phien.token());
    }

    private NguoiDung timNguoiDung(Long id) {
        return nguoiDungRepo.findById(id).orElseThrow(() -> ApiException.notFound("Không tìm thấy người dùng"));
    }

    static String rongThanhNull(String s) {
        return (s == null || s.isBlank()) ? null : s.trim();
    }
}
