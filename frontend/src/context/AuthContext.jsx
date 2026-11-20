import { createContext, useCallback, useContext, useEffect, useMemo, useState } from 'react';
import authService from '../services/authService.js';
import { TOKEN_KEY, USER_KEY } from '../services/api.js';

const AuthContext = createContext(null);

function docUserDaLuu() {
  try {
    return JSON.parse(localStorage.getItem(USER_KEY));
  } catch {
    return null;
  }
}

export function AuthProvider({ children }) {
  const coToken = !!localStorage.getItem(TOKEN_KEY);
  const [user, setUser] = useState(() => (coToken ? docUserDaLuu() : null));
  const [dangKhoiTao, setDangKhoiTao] = useState(coToken);

  const xoaPhien = useCallback(() => {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(USER_KEY);
    setUser(null);
  }, []);

  // Mo lai app: xac thuc lai token voi may chu
  useEffect(() => {
    if (!localStorage.getItem(TOKEN_KEY)) return;
    authService
      .me()
      .then((u) => {
        localStorage.setItem(USER_KEY, JSON.stringify(u));
        setUser(u);
      })
      .catch(() => xoaPhien())
      .finally(() => setDangKhoiTao(false));
  }, [xoaPhien]);

  // Token het han o bat ky API nao
  useEffect(() => {
    window.addEventListener('cineman:unauthorized', xoaPhien);
    return () => window.removeEventListener('cineman:unauthorized', xoaPhien);
  }, [xoaPhien]);

  const dangNhap = useCallback(async (tenDangNhap, matKhau) => {
    const data = await authService.dangNhap({ tenDangNhap, matKhau });
    localStorage.setItem(TOKEN_KEY, data.token);
    localStorage.setItem(USER_KEY, JSON.stringify(data.nguoiDung));
    setUser(data.nguoiDung);
    return data.nguoiDung;
  }, []);

  const dangXuat = useCallback(async () => {
    try {
      await authService.dangXuat();
    } catch {
      /* token da het han cung coi nhu da dang xuat */
    }
    xoaPhien();
  }, [xoaPhien]);

  const capNhatUser = useCallback((u) => {
    localStorage.setItem(USER_KEY, JSON.stringify(u));
    setUser(u);
  }, []);

  const value = useMemo(
    () => ({ user, dangNhap, dangXuat, capNhatUser }),
    [user, dangNhap, dangXuat, capNhatUser]
  );

  if (dangKhoiTao) return <div className="trang-cho">Đang tải...</div>;
  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error('useAuth phải được dùng bên trong <AuthProvider>');
  return ctx;
}
