package vn.cineman.entity;

import jakarta.persistence.*;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "ve")
public class Ve {
    private static final DateTimeFormatter NGAY = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final DateTimeFormatter GIO = DateTimeFormatter.ofPattern("HHmm");

    /** Khoa chinh, dung de phan biet rieng tung ve. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Ma ve: xac dinh ngay xem, suat chieu, ghe va phim (khong lien quan toi id cua ve).
     * Dinh dang yyyyMMdd-HHmm-M{phimId}-S{suatChieuId}-{maGhe}, vi du 20261121-1900-M1-S7-C5.
     * Trigger trg_ve_bi trong schema.sql kiem tra ma ve khop voi suat chieu va ghe.
     */
    @Column(name = "ma_ve", nullable = false, length = 40, unique = true)
    private String maVe;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "hoa_don_id", nullable = false)
    private HoaDon hoaDon;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "suat_chieu_id", nullable = false)
    private SuatChieu suatChieu;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ghe_id", nullable = false)
    private Ghe ghe;

    /** Sinh ma ve tu suat chieu va ghe. */
    public static String taoMaVe(SuatChieu suatChieu, Ghe ghe) {
        return suatChieu.getNgayChieu().format(NGAY) + "-" + suatChieu.getGioBatDau().format(GIO)
                + "-M" + suatChieu.getPhim().getId() + "-S" + suatChieu.getId() + "-" + ghe.getMaGhe();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public HoaDon getHoaDon() { return hoaDon; }
    public void setHoaDon(HoaDon hoaDon) { this.hoaDon = hoaDon; }

    public SuatChieu getSuatChieu() { return suatChieu; }
    public void setSuatChieu(SuatChieu suatChieu) { this.suatChieu = suatChieu; }

    public Ghe getGhe() { return ghe; }
    public void setGhe(Ghe ghe) { this.ghe = ghe; }

    public String getMaVe() { return maVe; }
    public void setMaVe(String maVe) { this.maVe = maVe; }

    /** Gia ve khong luu tren ve: lay theo loai ghe cua ghe (ghe -> loai_ghe.don_gia). */
    public Long getGiaVe() { return ghe.getLoaiGhe().getDonGia(); }

}
