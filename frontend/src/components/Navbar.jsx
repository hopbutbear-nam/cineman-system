import { NavLink, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext.jsx';
import { TEN_VAI_TRO, VAI_TRO } from '../utils/constants.js';

export default function Navbar() {
  const { user, dangXuat } = useAuth();
  const navigate = useNavigate();

  const handleDangXuat = async () => {
    await dangXuat();
    navigate('/login', { replace: true });
  };

  return (
    <header className="navbar">
      <div className="navbar__trai">
        <span className="logo">🎬 CineMan</span>
        <nav className="navbar__menu">
          <NavLink to="/" end>Trang chủ</NavLink>
          {user.vaiTro === VAI_TRO.KHACH_HANG && <NavLink to="/dang-ky-thanh-vien">Đăng ký thành viên</NavLink>}
          {user.vaiTro === VAI_TRO.NV_BAN_HANG && <NavLink to="/ban-ve">Bán vé tại quầy</NavLink>}
          <NavLink to="/ho-so">Thông tin cá nhân</NavLink>
          <NavLink to="/doi-mat-khau">Đổi mật khẩu</NavLink>
        </nav>
      </div>
      <div className="navbar__phai">
        <span className="navbar__user">
          {user.hoTen} <small>({TEN_VAI_TRO[user.vaiTro]})</small>
        </span>
        <button className="btn btn--vien" onClick={handleDangXuat}>Đăng xuất</button>
      </div>
    </header>
  );
}
