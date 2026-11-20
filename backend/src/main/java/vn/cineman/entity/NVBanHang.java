package vn.cineman.entity;

import jakarta.persistence.*;
import vn.cineman.security.VaiTro;

@Entity
@DiscriminatorValue("NV_BAN_HANG")
public class NVBanHang extends NhanVien {
    @Override
    public String getVaiTro() {
        return VaiTro.NV_BAN_HANG;
    }

}
