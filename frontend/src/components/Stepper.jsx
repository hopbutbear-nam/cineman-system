export default function Stepper({ cacBuoc, buocHienTai }) {
  return (
    <ol className="stepper">
      {cacBuoc.map((ten, i) => {
        const so = i + 1;
        const trangThai = so < buocHienTai ? 'xong' : so === buocHienTai ? 'hien-tai' : '';
        return (
          <li key={ten} className={`stepper__buoc ${trangThai}`}>
            <span className="stepper__so">{so < buocHienTai ? '✓' : so}</span>
            <span className="stepper__ten">{ten}</span>
          </li>
        );
      })}
    </ol>
  );
}
