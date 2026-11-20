package vn.cineman.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "phong_chieu")
public class PhongChieu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "rap_id", nullable = false)
    private RapChieuPhim rap;

    @Column(name = "ten_phong", nullable = false, length = 50)
    private String tenPhong;

    @Column(name = "suc_chua", nullable = false)
    private Integer sucChua;

    @Column(name = "mo_ta")
    private String moTa;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public RapChieuPhim getRap() { return rap; }
    public void setRap(RapChieuPhim rap) { this.rap = rap; }

    public String getTenPhong() { return tenPhong; }
    public void setTenPhong(String tenPhong) { this.tenPhong = tenPhong; }

    public Integer getSucChua() { return sucChua; }
    public void setSucChua(Integer sucChua) { this.sucChua = sucChua; }

    public String getMoTa() { return moTa; }
    public void setMoTa(String moTa) { this.moTa = moTa; }

}
