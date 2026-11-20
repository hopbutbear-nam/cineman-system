package vn.cineman.dto;

public record GheDto(
        Long id,
        String maGhe,
        String hang,
        Integer soGhe,
        String tenLoaiGhe,
        Long donGia,
        boolean daBan
) {
}
