import { useState } from 'react';
import authService from '../services/authService.js';
import { layLoi } from '../services/api.js';
import Alert from '../components/Alert.jsx';
import FormField from '../components/FormField.jsx';

const BAN_DAU = { matKhauHienTai: '', matKhauMoi: '', xacNhan: '' };

export default function ChangePasswordPage() {
  const [form, setForm] = useState(BAN_DAU);
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
    const errs = {};
    if (!form.matKhauHienTai) errs.matKhauHienTai = 'Vui lòng nhập mật khẩu hiện tại';
    if (form.matKhauMoi.length < 6) errs.matKhauMoi = 'Mật khẩu mới phải có ít nhất 6 ký tự';
    if (form.xacNhan !== form.matKhauMoi) errs.xacNhan = 'Xác nhận mật khẩu không khớp';
    setLoiTruong(errs);
    if (Object.keys(errs).length) return;

    setDangGui(true);
    try {
      await authService.doiMatKhau({ matKhauHienTai: form.matKhauHienTai, matKhauMoi: form.matKhauMoi });
      setForm(BAN_DAU);
      setThanhCong('Đổi mật khẩu thành công.');
    } catch (err) {
      const { message, fieldErrors } = layLoi(err);
      setLoiTruong(fieldErrors);
      setLoi(message);
    } finally {
      setDangGui(false);
    }
  };

  return (
    <form className="khung khung--hep" onSubmit={onSubmit} noValidate>
      <h1>Đổi mật khẩu</h1>
      <Alert loai="loi">{loi}</Alert>
      <Alert loai="thanh-cong">{thanhCong}</Alert>
      <FormField label="Mật khẩu hiện tại" name="matKhauHienTai" type="password" value={form.matKhauHienTai} onChange={onChange} error={loiTruong.matKhauHienTai} autoComplete="current-password" />
      <FormField label="Mật khẩu mới" name="matKhauMoi" type="password" value={form.matKhauMoi} onChange={onChange} error={loiTruong.matKhauMoi} autoComplete="new-password" />
      <FormField label="Nhập lại mật khẩu mới" name="xacNhan" type="password" value={form.xacNhan} onChange={onChange} error={loiTruong.xacNhan} autoComplete="new-password" />
      <button className="btn btn--chinh" disabled={dangGui}>{dangGui ? 'Đang lưu...' : 'Đổi mật khẩu'}</button>
    </form>
  );
}
