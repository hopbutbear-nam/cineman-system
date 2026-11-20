import api from './api.js';

const khachHangService = {
  trangThaiThanhVien: () => api.get('/khach-hang/thanh-vien').then((r) => r.data),
  dangKyThanhVien: (body) => api.post('/khach-hang/thanh-vien', body).then((r) => r.data),
};

export default khachHangService;
