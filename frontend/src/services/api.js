import axios from 'axios';

export const TOKEN_KEY = 'cineman_token';
export const USER_KEY = 'cineman_user';

const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '/api',
  timeout: 15000,
  headers: { 'Content-Type': 'application/json' },
});

// Gan Bearer token vao moi request
api.interceptors.request.use((config) => {
  const token = localStorage.getItem(TOKEN_KEY);
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

// Het phien (401) o cac API khac dang nhap => thong bao cho AuthContext dang xuat
api.interceptors.response.use(
  (res) => res,
  (error) => {
    const url = error.config?.url || '';
    if (error.response?.status === 401 && !url.includes('/auth/login')) {
      window.dispatchEvent(new Event('cineman:unauthorized'));
    }
    return Promise.reject(error);
  }
);

/** Chuan hoa loi axios -> { status, message, fieldErrors } */
export function layLoi(error) {
  if (error.response) {
    const d = error.response.data || {};
    return {
      status: error.response.status,
      message: d.message || 'Đã có lỗi xảy ra. Vui lòng thử lại.',
      fieldErrors: d.errors || {},
    };
  }
  if (error.request) {
    return {
      status: 0,
      message: 'Không kết nối được máy chủ. Hãy kiểm tra backend (cổng 8080) đang chạy.',
      fieldErrors: {},
    };
  }
  return { status: 0, message: error.message, fieldErrors: {} };
}

export default api;
