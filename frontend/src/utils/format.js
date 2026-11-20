export function formatTien(n) {
  return `${Number(n ?? 0).toLocaleString('vi-VN')} đ`;
}

/** '2026-10-20' -> '20/10/2026' (khong dung Date de tranh lech mui gio) */
export function formatNgay(iso) {
  if (!iso) return '';
  const [y, m, d] = iso.split('-');
  return `${d}/${m}/${y}`;
}

/** '19:00:00' -> '19:00' */
export function formatGio(t) {
  return t ? t.slice(0, 5) : '';
}

/** '2026-10-20T19:05:33.123' -> '19:05 20/10/2026' */
export function formatNgayGio(iso) {
  if (!iso) return '';
  const [ngay, gio] = iso.split('T');
  return `${formatGio(gio)} ${formatNgay(ngay)}`;
}

/** Ngay hom nay dang yyyy-MM-dd (gio may nguoi dung) */
export function homNayISO() {
  const d = new Date();
  const p = (x) => String(x).padStart(2, '0');
  return `${d.getFullYear()}-${p(d.getMonth() + 1)}-${p(d.getDate())}`;
}
