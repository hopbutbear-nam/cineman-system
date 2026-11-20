import { useState } from 'react';
import Stepper from '../components/Stepper.jsx';
import Alert from '../components/Alert.jsx';
import BuocTimPhim from '../components/banve/BuocTimPhim.jsx';
import BuocChonSuat from '../components/banve/BuocChonSuat.jsx';
import BuocChonGhe from '../components/banve/BuocChonGhe.jsx';
import BuocKhachHang from '../components/banve/BuocKhachHang.jsx';
import BuocThanhToan from '../components/banve/BuocThanhToan.jsx';
import BuocHoanTat from '../components/banve/BuocHoanTat.jsx';

const CAC_BUOC = ['Tìm phim', 'Suất chiếu', 'Chọn ghế', 'Khách hàng', 'Thanh toán', 'Hoàn tất'];

/** Nghiep vu 2: Nhan vien ban hang ban ve tai quay (wizard 6 buoc). */
export default function BanVePage() {
  const [buoc, setBuoc] = useState(1);
  const [phim, setPhim] = useState(null);
  const [suat, setSuat] = useState(null);
  const [gheDaChon, setGheDaChon] = useState([]);
  const [khachHang, setKhachHang] = useState(null); // null = khong gan khach hang
  const [hoaDon, setHoaDon] = useState(null);
  const [thongBao, setThongBao] = useState('');

  const lamMoi = () => {
    setBuoc(1);
    setPhim(null);
    setSuat(null);
    setGheDaChon([]);
    setKhachHang(null);
    setHoaDon(null);
    setThongBao('');
  };

  // Ghe vua bi ban o giao dich khac -> quay lai so do ghe de chon lai
  const xuLyXungDotGhe = (message) => {
    setGheDaChon([]);
    setThongBao(message);
    setBuoc(3);
  };

  return (
    <div className="khung khung--rong">
      <h1>Bán vé tại quầy</h1>
      <Stepper cacBuoc={CAC_BUOC} buocHienTai={buoc} />
      <Alert loai="loi">{thongBao}</Alert>

      {buoc === 1 && (
        <BuocTimPhim onChon={(p) => { setPhim(p); setSuat(null); setGheDaChon([]); setThongBao(''); setBuoc(2); }} />
      )}
      {buoc === 2 && (
        <BuocChonSuat
          phim={phim}
          onChon={(s) => { setSuat(s); setGheDaChon([]); setThongBao(''); setBuoc(3); }}
          onQuayLai={() => setBuoc(1)}
        />
      )}
      {buoc === 3 && (
        <BuocChonGhe
          suat={suat}
          phim={phim}
          gheDaChon={gheDaChon}
          setGheDaChon={setGheDaChon}
          onTiepTuc={() => { setThongBao(''); setBuoc(4); }}
          onQuayLai={() => { setThongBao(''); setBuoc(2); }}
        />
      )}
      {buoc === 4 && (
        <BuocKhachHang
          gheDaChon={gheDaChon}
          khachHang={khachHang}
          setKhachHang={setKhachHang}
          onThanhToan={() => setBuoc(5)}
          onQuayLai={() => setBuoc(3)}
          onHuy={lamMoi}
        />
      )}
      {buoc === 5 && (
        <BuocThanhToan
          suat={suat}
          gheDaChon={gheDaChon}
          khachHang={khachHang}
          onThanhCong={(hd) => { setHoaDon(hd); setBuoc(6); }}
          onXungDotGhe={xuLyXungDotGhe}
          onQuayLai={() => setBuoc(4)}
          onHuy={lamMoi}
        />
      )}
      {buoc === 6 && <BuocHoanTat hoaDon={hoaDon} onHoanTat={lamMoi} />}
    </div>
  );
}
