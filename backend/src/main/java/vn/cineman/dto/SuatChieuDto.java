package vn.cineman.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record SuatChieuDto(
        Long id,
        LocalDate ngayChieu,
        LocalTime gioBatDau,
        LocalTime gioKetThuc,
        Long phongChieuId,
        String tenPhong,
        long soGheTrong,
        long tongSoGhe
) {
}
