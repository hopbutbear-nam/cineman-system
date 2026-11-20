import { useState } from 'react';
import banVeService from '../../services/banVeService.js';
import { layLoi } from '../../services/api.js';
import { formatTien } from '../../utils/format.js';
import Alert from '../Alert.jsx';

/** Buoc 5 (tuy chon / extend): nhap ma the hoac tra cuu SDT; bo qua = khach vang lai. */
export default function BuocKhachHang({ gheDaChon, khachHang, setKhachHang, onThanhToan, onQuayLai, onHuy }) {
  const [maThe, setMaThe] = useState('');
  const [sdt, setSdt] = useState('');
  const [dangTra, setDangTra] = useState(false);
  const [loi, setLoi] = useState('');

  const tong = gheDaChon.reduce((s, g) => s + g.donGia, 0);
  const dsGhe = [...gheDaChon].sort((a, b) => a.maGhe.localeCompare(b.maGhe, undefined, { numeric: true }));

  const traCuu = async (params) => {
    setDangTra(true);
    setLoi('');
    try {
      setKhachHang(await banVeService.traCuuKhachHang(params));
    } catch (err) {
      setKhachHang(null);
      setLoi(layLoi(err).message);
    } finally {
      setDangTra(false);
    }
  };

  return (
    <section>
      <h2>Xác nhận thông tin vé</h2>
      <table className="bang">
        <thead><tr><th>TT</th><th>Ghế</th><th>Loại ghế</th><th className="phai">Giá tiền</th></tr></thead>
        <tbody>
          {dsGhe.map((g, i) => (
            <tr key={g.id}><td>{i + 1}</td><td><strong>{g.maGhe}</strong></td><td>{g.tenLoaiGhe}</td><td className="phai">{formatTien(g.donGia)}</td></tr>
          ))}
        </tbody>
        <tfoot><tr><td colSpan="3" className="phai"><strong>Tổng tiền</strong></td><td className="phai"><strong>{formatTien(tong)}</strong></td></tr></tfoot>
      </table>

      <h3>Khách hàng thân thiết <small>(không bắt buộc)</small></h3>
      <Alert loai="loi">{loi}</Alert>
      <div className="luoi-2">
        <div className="thanh-tim">
          <input value={maThe} onChange={(e) => setMaThe(e.target.value)} placeholder="Mã thẻ, VD: CINE99281" />
          <button type="button" className="btn btn--nho" disabled={dangTra || !maThe.trim()} onClick={() => traCuu({ maThe: maThe.trim() })}>
            Tra cứu mã thẻ
          </button>
        </div>
        <div className="thanh-tim">
          <input value={sdt} onChange={(e) => setSdt(e.target.value)} placeholder="Hoặc số điện thoại khách đã đăng ký" />
          <button type="button" className="btn btn--nho" disabled={dangTra || !sdt.trim()} onClick={() => traCuu({ soDienThoai: sdt.trim() })}>
            Tra cứu bằng SĐT
          </button>
        </div>
      </div>

      {khachHang ? (
        <div className="the-khach-hang">
          <div>
            <strong>{khachHang.hoTen}</strong> — {khachHang.soDienThoai ?? 'chưa có SĐT'}<br />
            Mã thẻ: <strong>{khachHang.maThe ?? 'chưa được cấp thẻ'}</strong> — Trạng thái: {khachHang.trangThai}
          </div>
          <button type="button" className="btn btn--nho btn--vien" onClick={() => setKhachHang(null)}>Bỏ chọn</button>
        </div>
      ) : (
        <p className="goi-y">Nếu khách không dùng thẻ hoặc không cung cấp thông tin, bỏ qua — hóa đơn sẽ không gắn khách hàng.</p>
      )}

      <div className="nhom-nut">
        <button className="btn btn--vien" onClick={onQuayLai}>Quay lại</button>
        <button className="btn btn--nguy-hiem" onClick={onHuy}>Hủy</button>
        <button className="btn btn--chinh" onClick={onThanhToan}>Thanh toán</button>
      </div>
    </section>
  );
}
