package vn.cineman.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "rap_chieu_phim")
public class RapChieuPhim {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ten_rap", nullable = false, length = 150)
    private String tenRap;

    @Column(name = "dia_chi")
    private String diaChi;

    @Column(name = "so_dien_thoai", length = 15)
    private String soDienThoai;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTenRap() { return tenRap; }
    public void setTenRap(String tenRap) { this.tenRap = tenRap; }

    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }

    public String getSoDienThoai() { return soDienThoai; }
    public void setSoDienThoai(String soDienThoai) { this.soDienThoai = soDienThoai; }

}
