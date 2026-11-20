package vn.cineman.security;

import java.time.Instant;

/** Mot phien dang nhap dang hieu luc (luu trong bo nho may chu). */
public record PhienDangNhap(String token, Long nguoiDungId, String vaiTro, Instant hetHan) {
}
