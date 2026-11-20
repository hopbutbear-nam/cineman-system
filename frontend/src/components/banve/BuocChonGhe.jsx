import { useEffect, useState } from 'react';
import banVeService from '../../services/banVeService.js';
import { layLoi } from '../../services/api.js';
import { formatGio, formatNgay, formatTien } from '../../utils/format.js';
import Alert from '../Alert.jsx';
import SeatMap from '../SeatMap.jsx';

/** Buoc 4: so do ghe cua phong chieu thuoc suat chieu -> chon 1 hoac nhieu ghe trong. */
export default function BuocChonGhe({ suat, phim, gheDaChon, setGheDaChon, onTiepTuc, onQuayLai }) {
  const [soDo, setSoDo] = useState(null);
  const [loi, setLoi] = useState('');

  useEffect(() => {
    banVeService
      .soDoGhe(suat.id)
      .then(setSoDo)
      .catch((err) => setLoi(layLoi(err).message));
  }, [suat.id]);

  const daChonIds = new Set(gheDaChon.map((g) => g.id));

  const toggle = (g) => {
    setLoi('');
    setGheDaChon(daChonIds.has(g.id) ? gheDaChon.filter((x) => x.id !== g.id) : [...gheDaChon, g]);
  };

  const tiepTuc = () => {
    if (gheDaChon.length === 0) {
      setLoi('Vui lòng chọn ít nhất một ghế');
      return;
    }
    onTiepTuc();
  };

  const tong = gheDaChon.reduce((s, g) => s + g.donGia, 0);
  const dsChon = [...gheDaChon].sort((a, b) => a.maGhe.localeCompare(b.maGhe, undefined, { numeric: true }));

  return (
    <section>
      <h2>Chọn ghế</h2>
      <p className="thong-tin-phim">
        <strong>{phim.tenPhim}</strong> — {formatGio(suat.gioBatDau)}–{formatGio(suat.gioKetThuc)}, {formatNgay(suat.ngayChieu)} — Phòng {suat.tenPhong}
      </p>
      <Alert loai="loi">{loi}</Alert>
      {!soDo && !loi && <p>Đang tải sơ đồ ghế...</p>}
      {soDo && <SeatMap danhSachGhe={soDo.danhSachGhe} daChonIds={daChonIds} onToggle={toggle} />}
      <div className="tom-tat-chon">
        Đã chọn: <strong>{dsChon.map((g) => g.maGhe).join(', ') || '—'}</strong> — Tạm tính: <strong>{formatTien(tong)}</strong>
      </div>
      <div className="nhom-nut">
        <button className="btn btn--vien" onClick={onQuayLai}>Quay lại</button>
        <button className="btn btn--chinh" onClick={tiepTuc}>Tiếp tục</button>
      </div>
    </section>
  );
}
