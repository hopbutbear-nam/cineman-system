import { useState } from 'react';
import { useAuth } from '../context/AuthContext.jsx';
import authService from '../services/authService.js';
import { layLoi } from '../services/api.js';
import { kiemTraHoSo } from '../utils/validate.js';
import { GIOI_TINH, TEN_VAI_TRO } from '../utils/constants.js';
import { homNayISO } from '../utils/format.js';
import Alert from '../components/Alert.jsx';
import FormField from '../components/FormField.jsx';

export default function ProfilePage() {
  const { user, capNhatUser } = useAuth();
  const [form, setForm] = useState({
    hoTen: user.hoTen ?? '',
    diaChi: user.diaChi ?? '',
    ngaySinh: user.ngaySinh ?? '',
    gioiTinh: user.gioiTinh ?? '',
    email: user.email ?? '',
    soDienThoai: user.soDienThoai ?? '',
  });
  const [loiTruong, setLoiTruong] = useState({});
  const [loi, setLoi] = useState('');
  const [thanhCong, setThanhCong] = useState('');
  const [dangGui, setDangGui] = useState(false);

  const onChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
    setLoiTruong({ ...loiTruong, [e.target.name]: undefined });
  };

  const onSubmit = async (e) => {
    e.preventDefault();
    setLoi('');
    setThanhCong('');
    const errs = kiemTraHoSo(form);
    setLoiTruong(errs);
    if (Object.keys(errs).length) return;

    setDangGui(true);
    try {
      const body = {
        hoTen: form.hoTen.trim(),
        diaChi: form.diaChi.trim() || null,
        ngaySinh: form.ngaySinh || null,
        gioiTinh: form.gioiTinh || null,
        email: form.email.trim() || null,
        soDienThoai: form.soDienThoai.trim() || null,
      };
      const moi = await authService.capNhatThongTin(body);
      capNhatUser(moi);
      setThanhCong('Cập nhật thông tin cá nhân thành công.');
    } catch (err) {
      const { message, fieldErrors } = layLoi(err);
      setLoiTruong(fieldErrors);
      setLoi(message);
    } finally {
      setDangGui(false);
    }
  };

  return (
    <form className="khung khung--vua" onSubmit={onSubmit} noValidate>
      <h1>Thông tin cá nhân</h1>
      <p className="mo-ta">
        Tên đăng nhập: <strong>{user.tenDangNhap}</strong> — Vai trò: <strong>{TEN_VAI_TRO[user.vaiTro]}</strong>
        {user.chucVu && <> — Chức vụ: <strong>{user.chucVu}</strong></>}
      </p>
      <Alert loai="loi">{loi}</Alert>
      <Alert loai="thanh-cong">{thanhCong}</Alert>
      <FormField label="Họ tên" name="hoTen" value={form.hoTen} onChange={onChange} error={loiTruong.hoTen} required />
      <FormField label="Số điện thoại" name="soDienThoai" value={form.soDienThoai} onChange={onChange} error={loiTruong.soDienThoai} />
      <FormField label="Email" name="email" value={form.email} onChange={onChange} error={loiTruong.email} />
      <FormField label="Giới tính" name="gioiTinh" value={form.gioiTinh} onChange={onChange} options={GIOI_TINH} error={loiTruong.gioiTinh} />
      <FormField label="Ngày sinh" name="ngaySinh" type="date" max={homNayISO()} value={form.ngaySinh} onChange={onChange} error={loiTruong.ngaySinh} />
      <FormField label="Địa chỉ" name="diaChi" value={form.diaChi} onChange={onChange} error={loiTruong.diaChi} />
      <button className="btn btn--chinh" disabled={dangGui}>{dangGui ? 'Đang lưu...' : 'Lưu thay đổi'}</button>
    </form>
  );
}
