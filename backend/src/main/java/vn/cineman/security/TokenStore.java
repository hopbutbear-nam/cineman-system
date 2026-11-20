package vn.cineman.security;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Kho phien dang nhap trong bo nho: token ngau nhien (opaque) -> phien.
 * Dang xuat = xoa token. Khoi dong lai backend thi phai dang nhap lai.
 */
@Component
public class TokenStore {
    private final Map<String, PhienDangNhap> cacPhien = new ConcurrentHashMap<>();
    private final SecureRandom random = new SecureRandom();
    private final Duration thoiHan;

    public TokenStore(@Value("${cineman.auth.token-ttl-minutes:480}") long phut) {
        this.thoiHan = Duration.ofMinutes(phut);
    }

    public PhienDangNhap taoPhien(Long nguoiDungId, String vaiTro) {
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        String token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        PhienDangNhap phien = new PhienDangNhap(token, nguoiDungId, vaiTro, Instant.now().plus(thoiHan));
        cacPhien.put(token, phien);
        return phien;
    }

    public Optional<PhienDangNhap> tim(String token) {
        if (token == null || token.isBlank()) {
            return Optional.empty();
        }
        PhienDangNhap phien = cacPhien.get(token);
        if (phien == null) {
            return Optional.empty();
        }
        if (phien.hetHan().isBefore(Instant.now())) {
            cacPhien.remove(token);
            return Optional.empty();
        }
        return Optional.of(phien);
    }

    public void huy(String token) {
        if (token != null) {
            cacPhien.remove(token);
        }
    }

    /** Huy moi phien cua nguoi dung tru phien dang dung (dung sau khi doi mat khau). */
    public void huyCacPhienKhac(Long nguoiDungId, String tokenGiuLai) {
        cacPhien.values().removeIf(p -> p.nguoiDungId().equals(nguoiDungId) && !p.token().equals(tokenGiuLai));
    }
}
