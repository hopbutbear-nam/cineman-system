package vn.cineman.entity;

import jakarta.persistence.*;

@Entity
public abstract class NhanVien extends NguoiDung {
    @Column(name = "chuc_vu", length = 50)
    private String chucVu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rap_id")
    private RapChieuPhim rap;

    public String getChucVu() { return chucVu; }
    public void setChucVu(String chucVu) { this.chucVu = chucVu; }

    public RapChieuPhim getRap() { return rap; }
    public void setRap(RapChieuPhim rap) { this.rap = rap; }

}
