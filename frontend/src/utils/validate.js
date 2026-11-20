import { homNayISO } from './format.js';

export const REGEX_SDT = /^(0|\+84)[35789]\d{8}$/;
export const REGEX_EMAIL = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

/** Kiem tra form dang ky thanh vien. Tra ve { truong: 'thong bao loi' } (rong = hop le). */
export function kiemTraDangKyThanhVien(v) {
  const e = {};
  if (!v.hoTen.trim()) e.hoTen = 'Họ tên không được để trống';
  if (!v.soDienThoai.trim()) e.soDienThoai = 'Số điện thoại không được để trống';
  else if (!REGEX_SDT.test(v.soDienThoai.trim())) e.soDienThoai = 'Số điện thoại không đúng định dạng (VD: 0912345678)';
  if (!v.email.trim()) e.email = 'Email không được để trống';
  else if (!REGEX_EMAIL.test(v.email.trim())) e.email = 'Email không đúng định dạng';
  if (!v.gioiTinh) e.gioiTinh = 'Vui lòng chọn giới tính';
  if (!v.ngaySinh) e.ngaySinh = 'Ngày sinh không được để trống';
  else if (v.ngaySinh >= homNayISO()) e.ngaySinh = 'Ngày sinh phải ở quá khứ';
  else if (v.ngaySinh < '1900-01-01') e.ngaySinh = 'Ngày sinh không hợp lệ';
  return e;
}

/** Kiem tra form cap nhat thong tin ca nhan (cho phep de trong SDT/email). */
export function kiemTraHoSo(v) {
  const e = {};
  if (!v.hoTen.trim()) e.hoTen = 'Họ tên không được để trống';
  if (v.soDienThoai.trim() && !REGEX_SDT.test(v.soDienThoai.trim())) e.soDienThoai = 'Số điện thoại không đúng định dạng (VD: 0912345678)';
  if (v.email.trim() && !REGEX_EMAIL.test(v.email.trim())) e.email = 'Email không đúng định dạng';
  if (v.ngaySinh && v.ngaySinh >= homNayISO()) e.ngaySinh = 'Ngày sinh phải ở quá khứ';
  return e;
}
