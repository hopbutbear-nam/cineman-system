package vn.cineman.entity;

import jakarta.persistence.*;
import vn.cineman.security.VaiTro;

@Entity
@DiscriminatorValue("KHACH_HANG")
public class KhachHang extends NguoiDung {
    public static final String CHUA_DANG_KY = "Chưa đăng ký";
    public static final String DA_DANG_KY = "Đã đăng ký";


    @Column(name = "trang_thai", length = 20)
    private String trangThai;

    @Override
    public String getVaiTro() {
        return VaiTro.KHACH_HANG;
    }


    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

}
