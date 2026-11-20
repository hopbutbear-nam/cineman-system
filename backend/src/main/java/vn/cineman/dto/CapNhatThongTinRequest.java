package vn.cineman.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record CapNhatThongTinRequest(
        @NotBlank(message = "Họ tên không được để trống") @Size(max = 100, message = "Họ tên tối đa 100 ký tự") String hoTen,
        @Size(max = 255, message = "Địa chỉ tối đa 255 ký tự") String diaChi,
        @Past(message = "Ngày sinh phải ở quá khứ") LocalDate ngaySinh,
        @Pattern(regexp = "^$|^(Nam|Nữ|Khác)$", message = "Giới tính phải là Nam, Nữ hoặc Khác") String gioiTinh,
        @Email(message = "Email không đúng định dạng") @Size(max = 100, message = "Email tối đa 100 ký tự") String email,
        @Pattern(regexp = "^$|^(0|\\+84)[35789]\\d{8}$", message = "Số điện thoại không đúng định dạng (VD: 0912345678)") String soDienThoai
) {
}
