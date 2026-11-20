import { NavLink, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext.jsx';
import { VAI_TRO } from '../utils/constants.js';

/**
 * Sidebar dọc bên trái, dùng chung cho cả khách hàng và nhân viên bán hàng.
 * Hiển thị menu điều hướng khác nhau tùy theo vai trò (user.vaiTro).
 */
export default function Sidebar() {
  const { user, dangXuat } = useAuth();
  const navigate = useNavigate();

  const handleDangXuat = async () => {
    await dangXuat();
    navigate('/login', { replace: true });
  };

  return (
    <aside className="sidebar">
      {/* Logo CineMan */}
      <div className="sidebar__logo">
        🎬 <span>CineMan</span>
      </div>

      {/* Menu điều hướng */}
      <nav className="sidebar__menu">
        <NavLink to="/" end className="sidebar__lien-ket">
          🏠 Trang chủ
        </NavLink>

        {/* Chỉ hiển thị cho khách hàng */}
        {user.vaiTro === VAI_TRO.KHACH_HANG && (
          <NavLink to="/dang-ky-thanh-vien" className="sidebar__lien-ket">
            🎟️ Đăng ký thành viên
          </NavLink>
        )}

        {/* Chỉ hiển thị cho nhân viên bán hàng */}
        {user.vaiTro === VAI_TRO.NV_BAN_HANG && (
          <NavLink to="/ban-ve" className="sidebar__lien-ket">
            🎫 Bán vé tại quầy
          </NavLink>
        )}

        <NavLink to="/ho-so" className="sidebar__lien-ket">
          👤 Thông tin cá nhân
        </NavLink>

        <NavLink to="/doi-mat-khau" className="sidebar__lien-ket">
          🔒 Đổi mật khẩu
        </NavLink>
      </nav>

      {/* Nút đăng xuất – đẩy xuống cuối sidebar bằng margin-top: auto */}
      <div className="sidebar__cuoi">
        <button className="sidebar__nut-dang-xuat" onClick={handleDangXuat}>
          🚪 Đăng xuất
        </button>
      </div>
    </aside>
  );
}
