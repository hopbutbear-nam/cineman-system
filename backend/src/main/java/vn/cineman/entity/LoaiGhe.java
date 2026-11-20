package vn.cineman.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "loai_ghe")
public class LoaiGhe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ten_loai", nullable = false, length = 50)
    private String tenLoai;

    @Column(name = "don_gia", nullable = false)
    private Long donGia;

    @Column(name = "mo_ta")
    private String moTa;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTenLoai() { return tenLoai; }
    public void setTenLoai(String tenLoai) { this.tenLoai = tenLoai; }

    public Long getDonGia() { return donGia; }
    public void setDonGia(Long donGia) { this.donGia = donGia; }

    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }

}
