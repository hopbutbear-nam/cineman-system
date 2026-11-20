package vn.cineman.dto;

import java.time.LocalDate;

public record ThanhVienDto(
        String trangThai,
        String maThe,
        String hoTen,
        String soDienThoai,
        String email,
        String gioiTinh,
        LocalDate ngaySinh
) {
}
