package vn.cineman.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record VeDto(
        Long id,
        String maVe,
        String tenPhim,
        String tenPhong,
        LocalDate ngayChieu,
        LocalTime gioBatDau,
        LocalTime gioKetThuc,
        String maGhe,
        String tenLoaiGhe,
        Long giaVe
) {
}
