import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext.jsx';
import khachHangService from '../services/khachHangService.js';
import { layLoi } from '../services/api.js';
import { kiemTraDangKyThanhVien } from '../utils/validate.js';
import { GIOI_TINH, TRANG_THAI_DA_DANG_KY } from '../utils/constants.js';
import { formatNgay, homNayISO } from '../utils/format.js';
import Alert from '../components/Alert.jsx';
import FormField from '../components/FormField.jsx';

/** Nghiep vu 1: Khach hang dang ky thanh vien. */
export default function DangKyThanhVienPage() {
  const { user, capNhatUser } = useAuth();
  const navigate = useNavigate();

  const [dangTai, setDangTai] = useState(true);
  const [daLaThanhVien, setDaLaThanhVien] = useState(false);
  const [maThe, setMaThe] = useState(null);
  const [thongTin, setThongTin] = useState(null); // thông tin khách hàng đã đăng ký
  const [form, setForm] = useState({ hoTen: '', soDienThoai: '', email: '', gioiTinh: '', ngaySinh: '' });
  const [loiTruong, setLoiTruong] = useState({});
  const [loi, setLoi] = useState('');
  const [dangGui, setDangGui] = useState(false);
  const [thongBaoThanhCong, setThongBaoThanhCong] = useState('');

  // Dien san thong tin hien co + kiem tra trang thai thanh vien
  useEffect(() => {
    khachHangService
      .trangThaiThanhVien()
      .then((tv) => {
        setDaLaThanhVien(tv.trangThai === TRANG_THAI_DA_DANG_KY);
        setMaThe(tv.maThe);
        setThongTin(tv);
        setForm({
          hoTen: tv.hoTen ?? '',
          soDienThoai: tv.soDienThoai ?? '',
          email: tv.email ?? '',
          gioiTinh: tv.gioiTinh ?? '',
          ngaySinh: tv.ngaySinh ?? '',
        });
      })
      .catch((err) => setLoi(layLoi(err).message))
      .finally(() => setDangTai(false));
  }, []);

  const onChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
    setLoiTruong({ ...loiTruong, [e.target.name]: undefined });
  };

  const onSubmit = async (e) => {
    e.preventDefault();
    setLoi('');
    const errs = kiemTraDangKyThanhVien(form);
    setLoiTruong(errs);
    if (Object.keys(errs).length) return;

    setDangGui(true);
    try {
      const kq = await khachHangService.dangKyThanhVien({
        hoTen: form.hoTen.trim(),
        soDienThoai: form.soDienThoai.trim(),
        email: form.email.trim(),
        gioiTinh: form.gioiTinh,
        ngaySinh: form.ngaySinh,
      });
      capNhatUser({
        ...user,
        hoTen: kq.thanhVien.hoTen,
        soDienThoai: kq.thanhVien.soDienThoai,
        email: kq.thanhVien.email,
        gioiTinh: kq.thanhVien.gioiTinh,
        ngaySinh: kq.thanhVien.ngaySinh,
        trangThaiThanhVien: kq.thanhVien.trangThai,
      });
      setThongBaoThanhCong(kq.thongBao);
    } catch (err) {
      const { message, fieldErrors } = layLoi(err);
      setLoiTruong(fieldErrors);
      setLoi(message);
    } finally {
      setDangGui(false);
    }
  };

  if (dangTai) return <div className="khung">Đang tải...</div>;

  // Man hinh thong bao ket qua
  if (thongBaoThanhCong) {
    return (
      <div className="khung khung--hep ket-qua">
        <div className="ket-qua__icon">✅</div>
        <h1>Đăng ký thành viên thành công!</h1>
        <p>Quý khách vui lòng tới nhận thẻ thành viên trực tiếp tại quầy lễ tân.</p>
        <button className="btn btn--chinh" onClick={() => navigate('/')}>Hoàn tất</button>
      </div>
    );
  }

  // Khách hàng đã đăng ký thành viên: hiện thông tin đã đăng ký;
  // nếu đã được cấp thẻ thì hiện thông tin thẻ, nếu chưa thì nhắc nhận thẻ tại quầy.
  if (daLaThanhVien) {
    return (
      <div className="khung khung--vua">
        <h1>Thông tin thành viên</h1>
        <p className="mo-ta">Bạn đã đăng ký thành viên. Thông tin đã đăng ký:</p>
        <dl className="thong-tin-tv">
          <dt>Họ tên</dt><dd>{thongTin?.hoTen}</dd>
          <dt>Số điện thoại</dt><dd>{thongTin?.soDienThoai}</dd>
          <dt>Email</dt><dd>{thongTin?.email}</dd>
          <dt>Giới tính</dt><dd>{thongTin?.gioiTinh}</dd>
          <dt>Ngày sinh</dt><dd>{formatNgay(thongTin?.ngaySinh)}</dd>
        </dl>
        {maThe ? (
          <div className="the-khach-hang">
            <span>🎟️ Thẻ thành viên</span>
            <strong>{maThe}</strong>
          </div>
        ) : (
          <div className="the-khach-hang the-khach-hang--cho">
            <span>Quý khách vui lòng tới quầy lễ tân để nhận thẻ thành viên.</span>
          </div>
        )}
        <div className="nhom-nut">
          <button className="btn btn--chinh" onClick={() => navigate('/')}>Về trang chủ</button>
        </div>
      </div>
    );
  }

  return (
    <form className="khung khung--vua" onSubmit={onSubmit} noValidate>
      <h1>Đăng ký thành viên</h1>
      <p className="mo-ta">Nhập thông tin cá nhân để tham gia chương trình khách hàng thân thiết.</p>
      <Alert loai="loi">{loi}</Alert>
      <FormField label="Họ tên" name="hoTen" value={form.hoTen} onChange={onChange} error={loiTruong.hoTen} required />
      <FormField label="Số điện thoại" name="soDienThoai" value={form.soDienThoai} onChange={onChange} error={loiTruong.soDienThoai} placeholder="0912345678" required />
      <FormField label="Email" name="email" value={form.email} onChange={onChange} error={loiTruong.email} placeholder="a@gmail.com" required />
      <FormField label="Giới tính" name="gioiTinh" value={form.gioiTinh} onChange={onChange} options={GIOI_TINH} error={loiTruong.gioiTinh} required />
      <FormField label="Ngày sinh" name="ngaySinh" type="date" max={homNayISO()} value={form.ngaySinh} onChange={onChange} error={loiTruong.ngaySinh} required />
      <div className="nhom-nut">
        <button type="button" className="btn btn--vien" onClick={() => navigate('/')}>Quay lại</button>
        <button className="btn btn--chinh" disabled={dangGui}>{dangGui ? 'Đang gửi...' : 'Đăng ký'}</button>
      </div>
    </form>
  );
}
