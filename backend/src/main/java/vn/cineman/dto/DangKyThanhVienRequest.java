package vn.cineman.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record DangKyThanhVienRequest(
        @NotBlank(message = "Họ tên không được để trống") @Size(max = 100, message = "Họ tên tối đa 100 ký tự") String hoTen,
        @NotBlank(message = "Số điện thoại không được để trống") @Pattern(regexp = "^(0|\\+84)[35789]\\d{8}$", message = "Số điện thoại không đúng định dạng (VD: 0912345678)") String soDienThoai,
        @NotBlank(message = "Email không được để trống") @Email(message = "Email không đúng định dạng") @Size(max = 100, message = "Email tối đa 100 ký tự") String email,
        @NotBlank(message = "Vui lòng chọn giới tính") @Pattern(regexp = "^(Nam|Nữ|Khác)$", message = "Giới tính phải là Nam, Nữ hoặc Khác") String gioiTinh,
        @NotNull(message = "Ngày sinh không được để trống") @Past(message = "Ngày sinh phải ở quá khứ") LocalDate ngaySinh
) {
}
