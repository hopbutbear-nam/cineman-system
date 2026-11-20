package vn.cineman.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DoiMatKhauRequest(
        @NotBlank(message = "Vui lòng nhập mật khẩu hiện tại") String matKhauHienTai,
        @NotBlank(message = "Vui lòng nhập mật khẩu mới") @Size(min = 6, max = 50, message = "Mật khẩu mới phải từ 6 đến 50 ký tự") String matKhauMoi
) {
}
