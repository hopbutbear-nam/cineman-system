package vn.cineman.dto;

import java.time.LocalDate;

public record PhimDto(
        Long id,
        String tenPhim,
        String theLoai,
        String daoDien,
        String dienVien,
        Integer thoiLuong,
        String gioiHanDoTuoi,
        LocalDate ngayKhoiChieu,
        LocalDate ngayKetThuc,
        String moTa,
        String posterUrl
) {
}
