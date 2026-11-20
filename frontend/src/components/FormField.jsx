/** Mot o nhap lieu co nhan + thong bao loi. Truyen `options` de render <select>. */
export default function FormField({
  label, name, value, onChange, error, type = 'text', required, options, placeholder, maxLength, max, autoComplete,
}) {
  return (
    <div className={`truong ${error ? 'truong--loi' : ''}`}>
      <label htmlFor={name}>
        {label}
        {required && <span className="bat-buoc"> *</span>}
      </label>
      {options ? (
        <select id={name} name={name} value={value} onChange={onChange}>
          <option value="">-- Chọn --</option>
          {options.map((o) => (
            <option key={o} value={o}>{o}</option>
          ))}
        </select>
      ) : (
        <input
          id={name} name={name} type={type} value={value} onChange={onChange}
          placeholder={placeholder} maxLength={maxLength} max={max} autoComplete={autoComplete}
        />
      )}
      {error && <div className="loi-truong">{error}</div>}
    </div>
  );
}
