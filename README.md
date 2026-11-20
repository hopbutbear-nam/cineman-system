# CineMan — Đăng ký thành viên & Bán vé tại quầy (Đề số 14)

Phạm vi: **2 nghiệp vụ** (Khách hàng đăng ký thành viên; Nhân viên bán hàng bán vé tại quầy)
+ đăng nhập / đăng xuất / đổi mật khẩu / cập nhật thông tin cá nhân.

| Tầng | Công nghệ |
|---|---|
| Backend | Java 25, Spring Boot 3.5.x (Maven), Spring Data JPA, Controller → Service → Repository |
| Frontend | React 19 + Vite, React Router, Axios |
| CSDL | MySQL 8.0 |

## Chạy trên máy local

1. **CSDL** — mở MySQL Workbench, chạy lần lượt `database/schema.sql` rồi `database/seed_data.sql`.
2. **Backend** (cổng 8080) — đặt mật khẩu MySQL rồi chạy:
   ```powershell
   cd backend
   $env:DB_PASSWORD = "mat_khau_mysql_cua_ban"   # mặc định: root / root
   mvn spring-boot:run
   ```
3. **Frontend** (cổng 5173):
   ```powershell
   cd frontend
   npm install
   npm run dev
   ```
4. Mở http://localhost:5173

## Tài khoản mẫu (mật khẩu `123456`)

| Tên đăng nhập | Vai trò | Ghi chú |
|---|---|---|
| `khach01` | Khách hàng | Chưa đăng ký thành viên, chưa có SĐT |
| `khach02` | Khách hàng | Chưa đăng ký thành viên, SĐT 0911111111 |
| `khach03` | Khách hàng | Đã là thành viên, thẻ `CINE99281`, SĐT 0987654321 |
| `khach04` | Khách hàng | Đã là thành viên, thẻ `CINE10002`, SĐT 0905555555 |
| `nvbh01`  | Nhân viên bán hàng | |

## API

| Method | URL | Vai trò | Mô tả |
|---|---|---|---|
| POST | `/api/auth/login` | — | Đăng nhập → token |
| POST | `/api/auth/logout` | mọi vai trò | Đăng xuất (hủy token) |
| GET/PUT | `/api/auth/me` | mọi vai trò | Xem / cập nhật thông tin cá nhân |
| PUT | `/api/auth/mat-khau` | mọi vai trò | Đổi mật khẩu |
| GET | `/api/khach-hang/thanh-vien` | KHACH_HANG | Trạng thái thành viên |
| POST | `/api/khach-hang/thanh-vien` | KHACH_HANG | **Đăng ký thành viên** |
| GET | `/api/ban-ve/phim?tuKhoa=` | NV_BAN_HANG | Tìm phim |
| GET | `/api/ban-ve/phim/{id}/suat-chieu` | NV_BAN_HANG | Suất chiếu sắp tới + số ghế trống |
| GET | `/api/ban-ve/suat-chieu/{id}/ghe` | NV_BAN_HANG | Sơ đồ ghế (trống / đã bán) |
| GET | `/api/ban-ve/khach-hang?maThe=` hoặc `?soDienThoai=` | NV_BAN_HANG | Nhận diện khách hàng (tùy chọn) |
| POST | `/api/ban-ve/thanh-toan` | NV_BAN_HANG | **Thanh toán**: 1 `@Transactional` tạo `HoaDon` + các `Ve` |

Xác thực: header `Authorization: Bearer <token>`; token lưu trong bộ nhớ máy chủ (khởi động lại backend → đăng nhập lại).

## Đẩy lên GitHub

```powershell
git add .
git commit -m "CineMan: dang ky thanh vien + ban ve tai quay"
git branch -M main
git remote add origin https://github.com/hopbutbear-nam/cineman-system.git
git push -u origin main
```

## Quy tắc dữ liệu chính (xem `database/schema.sql`)

- Giá vé chỉ gán vào loại ghế (`loai_ghe.don_gia`): **Thường**, **VIP**, **Sweetbox**. Bảng `ve` không lưu giá.
- `ve.ma_ve` = `yyyyMMdd-HHmm-M<phim_id>-S<suat_chieu_id>-<ma_ghe>`, xác định ngày xem, suất chiếu, ghế, phim; `ve.id` chỉ để phân biệt từng vé.
- Suất chiếu và ghế của vé phải cùng một phòng chiếu; một ghế chỉ bán một lần cho mỗi suất chiếu.
- Hóa đơn tại quầy bắt buộc có nhân viên lập; hóa đơn trực tuyến bắt buộc có khách hàng và phương thức "Thanh toán trực tuyến".
