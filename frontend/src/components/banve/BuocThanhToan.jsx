import { useState } from 'react';
import banVeService from '../../services/banVeService.js';
import { layLoi } from '../../services/api.js';
import { PHUONG_THUC_THANH_TOAN } from '../../utils/constants.js';
import { formatTien } from '../../utils/format.js';
import Alert from '../Alert.jsx';

/** Buoc 6: chon phuong thuc -> xac nhan => goi API luu hoa don + ve (1 giao dich). */
export default function BuocThanhToan({ suat, gheDaChon, khachHang, onThanhCong, onXungDotGhe, onQuayLai, onHuy }) {
  const [phuongThuc, setPhuongThuc] = useState(null);
  const [dangGui, setDangGui] = useState(false);
  const [loi, setLoi] = useState('');

  const tong = gheDaChon.reduce((s, g) => s + g.donGia, 0);

  const xacNhan = async () => {
    setDangGui(true);
    setLoi('');
    try {
      const hoaDon = await banVeService.thanhToan({
        suatChieuId: suat.id,
        gheIds: gheDaChon.map((g) => g.id),
        khachHangId: khachHang?.id ?? null,
        phuongThucThanhToan: phuongThuc,
      });
      onThanhCong(hoaDon);
    } catch (err) {
      const { status, message } = layLoi(err);
      if (status === 409) onXungDotGhe(message); // ghe vua duoc ban
      else setLoi(message);
    } finally {
      setDangGui(false);
    }
  };

  return (
    <section>
      <h2>Thanh toán</h2>
      <p className="tong-tien">Tổng tiền: <strong>{formatTien(tong)}</strong></p>
      <Alert loai="loi">{loi}</Alert>

      {!phuongThuc ? (
        <table className="bang">
          <thead><tr><th>TT</th><th>Phương thức thanh toán</th><th /></tr></thead>
          <tbody>
            {PHUONG_THUC_THANH_TOAN.map((pt, i) => (
              <tr key={pt}>
                <td>{i + 1}</td><td>{pt}</td>
                <td className="phai"><button className="btn btn--nho" onClick={() => setPhuongThuc(pt)}>Chọn</button></td>
              </tr>
            ))}
          </tbody>
        </table>
      ) : (
        <div className="the-khach-hang the-khach-hang--cho">
          <div>
            Phương thức thanh toán: <strong>{phuongThuc}</strong><br />
            Số tiền cần thu: <strong>{formatTien(tong)}</strong><br />
            <small>
              {phuongThuc === 'Tiền mặt'
                ? 'Nhận và đếm tiền từ khách hàng rồi bấm Xác nhận.'
                : 'Kiểm tra giao dịch chuyển khoản đã nhận rồi bấm Xác nhận.'}
            </small>
          </div>
          <button type="button" className="btn btn--nho btn--vien" onClick={() => setPhuongThuc(null)}>Đổi phương thức</button>
        </div>
      )}

      <div className="nhom-nut">
        <button className="btn btn--vien" onClick={onQuayLai} disabled={dangGui}>Quay lại</button>
        <button className="btn btn--nguy-hiem" onClick={onHuy} disabled={dangGui}>Hủy giao dịch</button>
        <button className="btn btn--chinh" onClick={xacNhan} disabled={!phuongThuc || dangGui}>
          {dangGui ? 'Đang xử lý...' : 'Xác nhận'}
        </button>
      </div>
    </section>
  );
}
