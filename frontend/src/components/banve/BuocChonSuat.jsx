import { useEffect, useState } from 'react';
import banVeService from '../../services/banVeService.js';
import { layLoi } from '../../services/api.js';
import { formatGio, formatNgay } from '../../utils/format.js';
import Alert from '../Alert.jsx';

/** Buoc 3: danh sach suat chieu cua phim da chon. */
export default function BuocChonSuat({ phim, onChon, onQuayLai }) {
  const [danhSach, setDanhSach] = useState(null);
  const [loi, setLoi] = useState('');

  useEffect(() => {
    banVeService
      .suatChieuCuaPhim(phim.id)
      .then(setDanhSach)
      .catch((err) => setLoi(layLoi(err).message));
  }, [phim.id]);

  return (
    <section>
      <h2>Chọn suất chiếu</h2>
      <p className="thong-tin-phim">
        <strong>{phim.tenPhim}</strong> — {phim.theLoai} — {phim.thoiLuong} phút — {phim.gioiHanDoTuoi}
      </p>
      <Alert loai="loi">{loi}</Alert>
      {danhSach === null && !loi && <p>Đang tải suất chiếu...</p>}
      {danhSach && danhSach.length === 0 && (
        <Alert loai="thong-tin">Phim này không còn suất chiếu sắp tới. Vui lòng báo khách hàng chọn phim khác.</Alert>
      )}
      {danhSach && danhSach.length > 0 && (
        <table className="bang">
          <thead>
            <tr><th>TT</th><th>Ngày chiếu</th><th>Giờ bắt đầu</th><th>Giờ kết thúc</th><th>Phòng</th><th>Ghế trống</th><th /></tr>
          </thead>
          <tbody>
            {danhSach.map((s, i) => (
              <tr key={s.id}>
                <td>{i + 1}</td>
                <td>{formatNgay(s.ngayChieu)}</td>
                <td>{formatGio(s.gioBatDau)}</td>
                <td>{formatGio(s.gioKetThuc)}</td>
                <td>{s.tenPhong}</td>
                <td>{s.soGheTrong} / {s.tongSoGhe}</td>
                <td className="phai">
                  {s.soGheTrong > 0
                    ? <button className="btn btn--nho" onClick={() => onChon(s)}>Chọn</button>
                    : <span className="nhan nhan--do">Hết ghế</span>}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
      <div className="nhom-nut"><button className="btn btn--vien" onClick={onQuayLai}>Quay lại</button></div>
    </section>
  );
}
