package vn.cineman.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record ThanhToanRequest(
        @NotNull(message = "Thiếu suất chiếu") Long suatChieuId,
        @NotEmpty(message = "Vui lòng chọn ít nhất một ghế") List<@NotNull(message = "Mã ghế không hợp lệ") Long> gheIds,
        Long khachHangId,
        @NotBlank(message = "Vui lòng chọn phương thức thanh toán") String phuongThucThanhToan
) {
}
