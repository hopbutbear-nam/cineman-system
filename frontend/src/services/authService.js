import api from './api.js';

const authService = {
  dangNhap: (body) => api.post('/auth/login', body).then((r) => r.data),
  dangXuat: () => api.post('/auth/logout').then((r) => r.data),
  me: () => api.get('/auth/me').then((r) => r.data),
  capNhatThongTin: (body) => api.put('/auth/me', body).then((r) => r.data),
  doiMatKhau: (body) => api.put('/auth/mat-khau', body).then((r) => r.data),
};

export default authService;
