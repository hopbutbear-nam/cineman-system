package vn.cineman.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "hoa_don")
public class HoaDon {
    public static final String LOAI_TAI_QUAY = "Tại quầy";
    public static final String LOAI_TRUC_TUYEN = "Trực tuyến";
    public static final String PT_TRUC_TUYEN = "Thanh toán trực tuyến";
    public static final String PT_TIEN_MAT = "Tiền mặt";
    public static final String PT_CHUYEN_KHOAN = "Chuyển khoản ngân hàng";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ngay_gio_lap", nullable = false)
    private LocalDateTime ngayGioLap;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "khach_hang_id")
    private KhachHang khachHang;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nv_ban_hang_id")
    private NVBanHang nvBanHang;

    @Column(name = "loai", nullable = false, length = 20)
    private String loai;

    @Column(name = "phuong_thuc_thanh_toan", nullable = false, length = 40)
    private String phuongThucThanhToan;

    @Column(name = "tong_tien", nullable = false)
    private Long tongTien;

    @OneToMany(mappedBy = "hoaDon", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Ve> danhSachVe = new ArrayList<>();

    public void themVe(Ve ve) {
        ve.setHoaDon(this);
        danhSachVe.add(ve);
    }


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getNgayGioLap() { return ngayGioLap; }
    public void setNgayGioLap(LocalDateTime ngayGioLap) { this.ngayGioLap = ngayGioLap; }

    public KhachHang getKhachHang() { return khachHang; }
    public void setKhachHang(KhachHang khachHang) { this.khachHang = khachHang; }

    public NVBanHang getNvBanHang() { return nvBanHang; }
    public void setNvBanHang(NVBanHang nvBanHang) { this.nvBanHang = nvBanHang; }

    public String getLoai() { return loai; }
    public void setLoai(String loai) { this.loai = loai; }

    public String getPhuongThucThanhToan() { return phuongThucThanhToan; }
    public void setPhuongThucThanhToan(String phuongThucThanhToan) { this.phuongThucThanhToan = phuongThucThanhToan; }

    public Long getTongTien() { return tongTien; }
    public void setTongTien(Long tongTien) { this.tongTien = tongTien; }

    public List<Ve> getDanhSachVe() { return danhSachVe; }
    public void setDanhSachVe(List<Ve> danhSachVe) { this.danhSachVe = danhSachVe; }

}
