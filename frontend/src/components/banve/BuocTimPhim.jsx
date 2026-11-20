import { useState } from 'react';
import banVeService from '../../services/banVeService.js';
import { layLoi } from '../../services/api.js';
import { formatNgay } from '../../utils/format.js';
import Alert from '../Alert.jsx';

/** Buoc 1-2: nhap tu khoa -> danh sach phim khop -> chon 1 phim. */
export default function BuocTimPhim({ onChon }) {
  const [tuKhoa, setTuKhoa] = useState('');
  const [ketQua, setKetQua] = useState(null); // null = chua tim
  const [dangTim, setDangTim] = useState(false);
  const [loi, setLoi] = useState('');

  const tim = async (e) => {
    e.preventDefault();
    setDangTim(true);
    setLoi('');
    try {
      setKetQua(await banVeService.timPhim(tuKhoa.trim()));
    } catch (err) {
      setLoi(layLoi(err).message);
    } finally {
      setDangTim(false);
    }
  };

  return (
    <section>
      <h2>Tìm phim</h2>
      <form className="thanh-tim" onSubmit={tim}>
        <input
          value={tuKhoa} onChange={(e) => setTuKhoa(e.target.value)}
          placeholder="Nhập tên phim để tìm kiếm (để trống = tất cả phim đang chiếu)" autoFocus
        />
        <button className="btn btn--chinh" disabled={dangTim}>{dangTim ? 'Đang tìm...' : 'Tìm kiếm'}</button>
      </form>
      <Alert loai="loi">{loi}</Alert>

      {ketQua && ketQua.length === 0 && <Alert loai="thong-tin">Không tìm thấy phim</Alert>}
      {ketQua && ketQua.length > 0 && (
        <table className="bang">
          <thead>
            <tr><th>TT</th><th>Tên phim</th><th>Thể loại</th><th>Thời lượng</th><th>Độ tuổi</th><th>Chiếu đến</th><th /></tr>
          </thead>
          <tbody>
            {ketQua.map((p, i) => (
              <tr key={p.id}>
                <td>{i + 1}</td>
                <td><strong>{p.tenPhim}</strong></td>
                <td>{p.theLoai}</td>
                <td>{p.thoiLuong} phút</td>
                <td>{p.gioiHanDoTuoi}</td>
                <td>{formatNgay(p.ngayKetThuc)}</td>
                <td className="phai"><button className="btn btn--nho" onClick={() => onChon(p)}>Chọn</button></td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </section>
  );
}
