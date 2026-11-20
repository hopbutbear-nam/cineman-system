package vn.cineman.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "ghe")
public class Ghe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "phong_chieu_id", nullable = false)
    private PhongChieu phongChieu;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "loai_ghe_id", nullable = false)
    private LoaiGhe loaiGhe;

    @Column(name = "ma_ghe", nullable = false, length = 10)
    private String maGhe;

    @Column(name = "hang", nullable = false, length = 2)
    private String hang;

    @Column(name = "so_ghe", nullable = false)
    private Integer soGhe;

    @Column(name = "mo_ta")
    private String moTa;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public PhongChieu getPhongChieu() { return phongChieu; }
    public void setPhongChieu(PhongChieu phongChieu) { this.phongChieu = phongChieu; }

    public LoaiGhe getLoaiGhe() { return loaiGhe; }
    public void setLoaiGhe(LoaiGhe loaiGhe) { this.loaiGhe = loaiGhe; }

    public String getMaGhe() { return maGhe; }
    public void setMaGhe(String maGhe) { this.maGhe = maGhe; }

    public String getHang() { return hang; }
    public void setHang(String hang) { this.hang = hang; }

    public Integer getSoGhe() { return soGhe; }
    public void setSoGhe(Integer soGhe) { this.soGhe = soGhe; }

    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }

}
