package vn.cineman.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record SoDoGheDto(
        Long suatChieuId,
        String tenPhim,
        String tenPhong,
        LocalDate ngayChieu,
        LocalTime gioBatDau,
        LocalTime gioKetThuc,
        List<GheDto> danhSachGhe
) {
}
