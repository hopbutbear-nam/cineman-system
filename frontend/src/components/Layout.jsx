import { Outlet } from 'react-router-dom';
import Sidebar from './Sidebar.jsx';
import { useAuth } from '../context/AuthContext.jsx';
import { VAI_TRO } from '../utils/constants.js';

export default function Layout() {
  const { user } = useAuth();

  // Lời chào thay đổi theo vai trò
  const loiChao =
    user.vaiTro === VAI_TRO.NV_BAN_HANG
      ? `Chào mừng nhân viên bán hàng ${user.hoTen}`
      : `Chào mừng khách hàng ${user.hoTen}`;

  return (
    <div className="bo-cuc cineman-page-background">
      {/* Khu vực A: Sidebar bên trái */}
      <Sidebar />

      {/* Khu vực B: Nội dung chính bên phải */}
      <div className="bo-cuc__phai">
        {/* Thanh đầu trang – nền trắng, hiển thị lời chào */}
        <header className="dau-trang">
          <span className="dau-trang__loi-chao">{loiChao}</span>
        </header>

        {/* Nội dung từng trang */}
        <main className="noi-dung">
          <Outlet />
        </main>
      </div>
    </div>
  );
}
