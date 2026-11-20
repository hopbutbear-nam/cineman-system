/** loai: 'loi' | 'thanh-cong' | 'thong-tin' */
export default function Alert({ loai = 'thong-tin', children }) {
  if (!children) return null;
  return <div className={`alert alert--${loai}`} role="alert">{children}</div>;
}
