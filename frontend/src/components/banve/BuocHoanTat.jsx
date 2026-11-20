import HoaDonVe from '../HoaDonVe.jsx';
import Alert from '../Alert.jsx';

export default function BuocHoanTat({ hoaDon, onHoanTat }) {
  return (
    <section>
      <Alert loai="thanh-cong">Thanh toán thành công! Vui lòng in vé kèm hóa đơn và giao cho khách hàng.</Alert>
      <HoaDonVe hoaDon={hoaDon} />
      <div className="nhom-nut khong-in">
        <button className="btn btn--vien" onClick={() => window.print()}>🖨️ In vé và hóa đơn</button>
        <button className="btn btn--chinh" onClick={onHoanTat}>Hoàn tất</button>
      </div>
    </section>
  );
}
