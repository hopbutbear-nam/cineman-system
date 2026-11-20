package vn.cineman.dto;

public record LoginResponse(
        String token,
        NguoiDungDto nguoiDung
) {
}
