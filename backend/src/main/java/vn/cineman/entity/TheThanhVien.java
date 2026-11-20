package vn.cineman.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "the_thanh_vien")
public class TheThanhVien {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ma_the", nullable = false, unique = true, length = 20)
    private String maThe;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "khach_hang_id", nullable = false, unique = true)
    private KhachHang khachHang;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nv_cap_id")
    private NVBanHang nvCap;

    @Column(name = "ngay_cap", nullable = false)
    private LocalDate ngayCap;

    @Column(name = "mo_ta")
    private String moTa;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMaThe() { return maThe; }
    public void setMaThe(String maThe) { this.maThe = maThe; }

    public KhachHang getKhachHang() { return khachHang; }
    public void setKhachHang(KhachHang khachHang) { this.khachHang = khachHang; }

    public NVBanHang getNvCap() { return nvCap; }
    public void setNvCap(NVBanHang nvCap) { this.nvCap = nvCap; }

    public LocalDate getNgayCap() { return ngayCap; }
    public void setNgayCap(LocalDate ngayCap) { this.ngayCap = ngayCap; }

    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }

}
