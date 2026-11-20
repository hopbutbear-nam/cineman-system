import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext.jsx';
import { TEN_VAI_TRO, TRANG_THAI_DA_DANG_KY, VAI_TRO } from '../utils/constants.js';

export default function HomePage() {
  const { user } = useAuth();
  const laThanhVien = user.trangThaiThanhVien === TRANG_THAI_DA_DANG_KY;

  return (
    <div className="khung">
      <h1>Xin chào, {user.hoTen}!</h1>
      <p className="mo-ta">Vai trò: <strong>{TEN_VAI_TRO[user.vaiTro]}</strong></p>

      <div className="luoi-the">
        {user.vaiTro === VAI_TRO.KHACH_HANG && (
          <Link to="/dang-ky-thanh-vien" className="the">
            <h3>🎟️ Đăng ký thành viên</h3>
            <p>{laThanhVien ? 'Bạn đã là thành viên của CineMan.' : 'Đăng ký để nhận thẻ thành viên tại quầy.'}</p>
            <span className={`nhan ${laThanhVien ? 'nhan--xanh' : 'nhan--xam'}`}>{user.trangThaiThanhVien}</span>
          </Link>
        )}
        {user.vaiTro === VAI_TRO.NV_BAN_HANG && (
          <Link to="/ban-ve" className="the">
            <h3>🎫 Bán vé tại quầy</h3>
            <p>Tìm phim, chọn suất chiếu, chọn ghế, thanh toán và in vé.</p>
          </Link>
        )}
        <Link to="/ho-so" className="the">
          <h3>👤 Thông tin cá nhân</h3>
          <p>Xem và cập nhật hồ sơ của bạn.</p>
        </Link>
        <Link to="/doi-mat-khau" className="the">
          <h3>🔒 Đổi mật khẩu</h3>
          <p>Thay đổi mật khẩu đăng nhập.</p>
        </Link>
      </div>
    </div>
  );
}
