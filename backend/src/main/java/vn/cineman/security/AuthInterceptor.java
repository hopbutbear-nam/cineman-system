package vn.cineman.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Arrays;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import vn.cineman.exception.ApiException;

/** Xac thuc Bearer token + kiem tra vai tro (@YeuCauVaiTro) cho moi /api/** (tru dang nhap). */
@Component
public class AuthInterceptor implements HandlerInterceptor {
    public static final String ATTR_PHIEN = "cineman.phienDangNhap";

    private final TokenStore tokenStore;

    public AuthInterceptor(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod()) || !(handler instanceof HandlerMethod hm)) {
            return true;
        }
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        String token = (header != null && header.startsWith("Bearer ")) ? header.substring(7).trim() : null;

        PhienDangNhap phien = tokenStore.tim(token).orElseThrow(() ->
                ApiException.unauthorized("Phiên đăng nhập không hợp lệ hoặc đã hết hạn. Vui lòng đăng nhập lại."));

        YeuCauVaiTro yeuCau = hm.getMethodAnnotation(YeuCauVaiTro.class);
        if (yeuCau == null) {
            yeuCau = hm.getBeanType().getAnnotation(YeuCauVaiTro.class);
        }
        if (yeuCau != null && !Arrays.asList(yeuCau.value()).contains(phien.vaiTro())) {
            throw ApiException.forbidden("Bạn không có quyền sử dụng chức năng này");
        }
        request.setAttribute(ATTR_PHIEN, phien);
        return true;
    }
}
