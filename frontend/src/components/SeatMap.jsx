/** So do ghe: ghe trong / ghe da ban / ghe dang chon; phan biet ghe VIP va Sweetbox. */
export default function SeatMap({ danhSachGhe, daChonIds, onToggle }) {
  const theoHang = danhSachGhe.reduce((acc, g) => {
    (acc[g.hang] ||= []).push(g);
    return acc;
  }, {});

  return (
    <div className="so-do-ghe">
      <div className="man-hinh">MÀN HÌNH</div>
      {Object.keys(theoHang).sort().map((hang) => (
        <div className="hang-ghe" key={hang}>
          <span className="hang-ghe__ten">{hang}</span>
          {theoHang[hang].map((g) => {
            const chon = daChonIds.has(g.id);
            const lop = ['ghe', g.daBan ? 'ghe--daban' : chon ? 'ghe--chon' : 'ghe--trong', g.tenLoaiGhe === 'VIP' ? 'ghe--vip' : '', g.tenLoaiGhe === 'Sweetbox' ? 'ghe--sweetbox' : '']
              .filter(Boolean).join(' ');
            return (
              <button
                key={g.id} type="button" className={lop} disabled={g.daBan}
                title={`${g.maGhe} - ${g.tenLoaiGhe} - ${g.donGia.toLocaleString('vi-VN')} đ${g.daBan ? ' (đã bán)' : ''}`}
                onClick={() => onToggle(g)}
              >
                {g.soGhe}
              </button>
            );
          })}
        </div>
      ))}
      <div className="chu-thich">
        <span><i className="ghe ghe--trong" /> Trống</span>
        <span><i className="ghe ghe--trong ghe--vip" /> VIP trống</span>
        <span><i className="ghe ghe--trong ghe--sweetbox" /> Sweetbox trống</span>
        <span><i className="ghe ghe--chon" /> Đang chọn</span>
        <span><i className="ghe ghe--daban" /> Đã bán</span>
      </div>
    </div>
  );
}
