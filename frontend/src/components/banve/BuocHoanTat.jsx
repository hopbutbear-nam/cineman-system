import { useState } from 'react';
import HoaDonVe from '../HoaDonVe.jsx';
import Alert from '../Alert.jsx';

/**
 * Bước 6 – Hoàn tất giao dịch.
 * Hiển thị hóa đơn và vé sau khi thanh toán thành công.
 * Nút IN sẽ hiện popup mô phỏng thay vì gọi window.print() thật.
 * Khi nhân viên nhấn OK trong popup, hệ thống gọi onHoanTat() để quay lại bước 1.
 */
export default function BuocHoanTat({ hoaDon, onHoanTat }) {
  // Trạng thái kiểm soát việc hiển thị popup thông báo in
  const [hienPopup, setHienPopup] = useState(false);

  // Nhấn nút IN → chỉ hiện popup, KHÔNG gọi window.print()
  const handleInVe = () => {
    setHienPopup(true);
  };

  // Nhấn OK trong popup → đóng popup rồi gọi onHoanTat() để về bước 1
  const handleOk = () => {
    setHienPopup(false);
    onHoanTat();
  };

  return (
    <section>
      <Alert loai="thanh-cong">Thanh toán thành công! Vui lòng in vé kèm hóa đơn và giao cho khách hàng.</Alert>
      <HoaDonVe hoaDon={hoaDon} />

      {/* Chỉ có nút IN, không có nút Hoàn tất riêng để tránh xung đột */}
      <div className="nhom-nut">
        <button className="btn btn--vien" onClick={handleInVe}>
          🖨️ IN
        </button>
      </div>

      {/* Popup mô phỏng in – hiện khi hienPopup = true */}
      {hienPopup && (
        <div className="popup-nen">
          <div className="popup-hop">
            <div className="popup-icon">✓</div>
            <p className="popup-noi-dung">In vé và hóa đơn thành công</p>
            <button className="btn btn--chinh" onClick={handleOk}>
              OK
            </button>
          </div>
        </div>
      )}
    </section>
  );
}
