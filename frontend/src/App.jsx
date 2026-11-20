import { Navigate, Route, Routes } from 'react-router-dom';
import Layout from './components/Layout.jsx';
import ProtectedRoute from './components/ProtectedRoute.jsx';
import LoginPage from './pages/LoginPage.jsx';
import HomePage from './pages/HomePage.jsx';
import ProfilePage from './pages/ProfilePage.jsx';
import ChangePasswordPage from './pages/ChangePasswordPage.jsx';
import DangKyThanhVienPage from './pages/DangKyThanhVienPage.jsx';
import BanVePage from './pages/BanVePage.jsx';
import { VAI_TRO } from './utils/constants.js';

export default function App() {
  return (
    <Routes>
      <Route path="/login" element={<LoginPage />} />

      {/* Cac trang can dang nhap */}
      <Route element={<ProtectedRoute />}>
        <Route element={<Layout />}>
          <Route path="/" element={<HomePage />} />
          <Route path="/ho-so" element={<ProfilePage />} />
          <Route path="/doi-mat-khau" element={<ChangePasswordPage />} />

          {/* Nghiep vu 1: chi KhachHang */}
          <Route element={<ProtectedRoute roles={[VAI_TRO.KHACH_HANG]} />}>
            <Route path="/dang-ky-thanh-vien" element={<DangKyThanhVienPage />} />
          </Route>

          {/* Nghiep vu 2: chi NVBanHang */}
          <Route element={<ProtectedRoute roles={[VAI_TRO.NV_BAN_HANG]} />}>
            <Route path="/ban-ve" element={<BanVePage />} />
          </Route>
        </Route>
      </Route>

      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  );
}
