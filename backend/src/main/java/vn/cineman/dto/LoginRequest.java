package vn.cineman.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "Vui lòng nhập tên đăng nhập") String tenDangNhap,
        @NotBlank(message = "Vui lòng nhập mật khẩu") String matKhau
) {
}
