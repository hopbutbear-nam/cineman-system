import { useState } from 'react';
import { Navigate, useLocation, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext.jsx';
import { layLoi } from '../services/api.js';
import Alert from '../components/Alert.jsx';
import FormField from '../components/FormField.jsx';

export default function LoginPage() {
  const { user, dangNhap } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const [form, setForm] = useState({ tenDangNhap: '', matKhau: '' });
  const [loi, setLoi] = useState('');
  const [dangGui, setDangGui] = useState(false);

  if (user) return <Navigate to="/" replace />;

  const onChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

  const onSubmit = async (e) => {
    e.preventDefault();
    if (!form.tenDangNhap.trim() || !form.matKhau) {
      setLoi('Vui lòng nhập tên đăng nhập và mật khẩu');
      return;
    }
    setDangGui(true);
    setLoi('');
    try {
      await dangNhap(form.tenDangNhap.trim(), form.matKhau);
      navigate(location.state?.from?.pathname || '/', { replace: true });
    } catch (err) {
      setLoi(layLoi(err).message);
    } finally {
      setDangGui(false);
    }
  };

  return (
    <div className="trang-dang-nhap">
      <form className="khung khung--hep" onSubmit={onSubmit}>
        <h1 className="logo logo--lon">🎬 CineMan</h1>
        <p className="mo-ta">Đăng nhập để sử dụng hệ thống</p>
        <Alert loai="loi">{loi}</Alert>
        <FormField label="Tên đăng nhập" name="tenDangNhap" value={form.tenDangNhap} onChange={onChange} autoComplete="username" />
        <FormField label="Mật khẩu" name="matKhau" type="password" value={form.matKhau} onChange={onChange} autoComplete="current-password" />
        <button className="btn btn--chinh btn--rong" disabled={dangGui}>
          {dangGui ? 'Đang đăng nhập...' : 'Đăng nhập'}
        </button>
        <div className="goi-y">
          <strong>Tài khoản mẫu</strong> (mật khẩu: <code>123456</code>)<br />
          Khách hàng: <code>khach01</code>, <code>khach02</code> (chưa là thành viên), <code>khach03</code><br />
          Nhân viên bán hàng: <code>nvbh01</code>
        </div>
      </form>
    </div>
  );
}
