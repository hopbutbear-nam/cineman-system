package vn.cineman.dto;

import java.time.LocalDate;

public record NguoiDungDto(
        Long id,
        String tenDangNhap,
        String hoTen,
        String vaiTro,
        String diaChi,
        LocalDate ngaySinh,
        String gioiTinh,
        String email,
        String soDienThoai,
        String chucVu,
        String trangThaiThanhVien
) {
}
