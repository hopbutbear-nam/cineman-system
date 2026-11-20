import { formatGio, formatNgay, formatNgayGio, formatTien } from '../utils/format.js';

/** Hoa don + ve de in (chi vung .vung-in duoc in, xem index.css). */
export default function HoaDonVe({ hoaDon }) {
  return (
    <div className="vung-in">
      <div className="hoa-don">
        <h2>CINEMAN - HÓA ĐƠN BÁN VÉ</h2>
        <p>Số hóa đơn: <strong>HD{String(hoaDon.id).padStart(6, '0')}</strong></p>
        <p>Thời gian lập: {formatNgayGio(hoaDon.ngayGioLap)}</p>
        <p>Loại: {hoaDon.loai} — Nhân viên: {hoaDon.tenNhanVien}</p>
        <p>
          Khách hàng: {hoaDon.tenKhachHang ?? 'Khách vãng lai'}
          {hoaDon.maThe && <> (Thẻ: {hoaDon.maThe})</>}
        </p>
        <p>Phương thức thanh toán: {hoaDon.phuongThucThanhToan}</p>
        <table className="bang">
          <thead>
            <tr><th>TT</th><th>Phim</th><th>Suất chiếu</th><th>Phòng</th><th>Ghế</th><th>Loại</th><th className="phai">Giá vé</th></tr>
          </thead>
          <tbody>
            {hoaDon.danhSachVe.map((v, i) => (
              <tr key={v.id}>
                <td>{i + 1}</td>
                <td>{v.tenPhim}</td>
                <td>{formatGio(v.gioBatDau)} – {formatGio(v.gioKetThuc)} {formatNgay(v.ngayChieu)}</td>
                <td>{v.tenPhong}</td>
                <td><strong>{v.maGhe}</strong></td>
                <td>{v.tenLoaiGhe}</td>
                <td className="phai">{formatTien(v.giaVe)}</td>
              </tr>
            ))}
          </tbody>
          <tfoot>
            <tr><td colSpan="6" className="phai"><strong>Tổng tiền</strong></td><td className="phai"><strong>{formatTien(hoaDon.tongTien)}</strong></td></tr>
          </tfoot>
        </table>
      </div>

      {hoaDon.danhSachVe.map((v) => (
        <div className="ve" key={`ve-${v.id}`}>
          <div className="ve__tieu-de">🎬 CINEMAN — VÉ XEM PHIM</div>
          <div className="ve__phim">{v.tenPhim}</div>
          <div>Suất: <strong>{formatGio(v.gioBatDau)}</strong> – {formatGio(v.gioKetThuc)}, ngày {formatNgay(v.ngayChieu)}</div>
          <div>Phòng: <strong>{v.tenPhong}</strong> &nbsp; Ghế: <strong>{v.maGhe}</strong> ({v.tenLoaiGhe})</div>
          <div>Giá vé: {formatTien(v.giaVe)} &nbsp; Mã vé: <strong>{v.maVe}</strong></div>
        </div>
      ))}
    </div>
  );
}
