package vn.cineman.dto;

import java.time.LocalDateTime;
import java.util.List;

public record HoaDonDto(
        Long id,
        LocalDateTime ngayGioLap,
        String loai,
        String phuongThucThanhToan,
        Long tongTien,
        String tenNhanVien,
        String tenKhachHang,
        String maThe,
        List<VeDto> danhSachVe
) {
}
