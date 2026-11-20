import api from './api.js';

const banVeService = {
  timPhim: (tuKhoa) => api.get('/ban-ve/phim', { params: { tuKhoa } }).then((r) => r.data),
  suatChieuCuaPhim: (phimId) => api.get(`/ban-ve/phim/${phimId}/suat-chieu`).then((r) => r.data),
  soDoGhe: (suatChieuId) => api.get(`/ban-ve/suat-chieu/${suatChieuId}/ghe`).then((r) => r.data),
  traCuuKhachHang: (params) => api.get('/ban-ve/khach-hang', { params }).then((r) => r.data),
  thanhToan: (body) => api.post('/ban-ve/thanh-toan', body).then((r) => r.data),
};

export default banVeService;
