package vn.cineman.service;

import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.cineman.dto.GheDto;
import vn.cineman.dto.HoaDonDto;
import vn.cineman.dto.KhachHangTraCuuDto;
import vn.cineman.dto.PhimDto;
import vn.cineman.dto.SoDoGheDto;
import vn.cineman.dto.SuatChieuDto;
import vn.cineman.dto.ThanhToanRequest;
import vn.cineman.entity.Ghe;
import vn.cineman.entity.HoaDon;
import vn.cineman.entity.KhachHang;
import vn.cineman.entity.NVBanHang;
import vn.cineman.entity.SuatChieu;
import vn.cineman.entity.TheThanhVien;
import vn.cineman.entity.Ve;
import vn.cineman.exception.ApiException;
import vn.cineman.repository.GheRepository;
import vn.cineman.repository.HoaDonRepository;
import vn.cineman.repository.KhachHangRepository;
import vn.cineman.repository.NVBanHangRepository;
import vn.cineman.repository.PhimRepository;
import vn.cineman.repository.SuatChieuRepository;
import vn.cineman.repository.TheThanhVienRepository;
import vn.cineman.repository.VeRepository;
import vn.cineman.util.ThoiGian;

/** Nghiep vu 2: Nhan vien ban hang ban ve tai quay. */
@Service
public class BanVeService {
    private final PhimRepository phimRepo;
    private final SuatChieuRepository suatChieuRepo;
    private final GheRepository gheRepo;
    private final VeRepository veRepo;
    private final HoaDonRepository hoaDonRepo;
    private final KhachHangRepository khachHangRepo;
    private final TheThanhVienRepository theRepo;
    private final NVBanHangRepository nvRepo;

    public BanVeService(PhimRepository phimRepo, SuatChieuRepository suatChieuRepo, GheRepository gheRepo,
                        VeRepository veRepo, HoaDonRepository hoaDonRepo, KhachHangRepository khachHangRepo,
                        TheThanhVienRepository theRepo, NVBanHangRepository nvRepo) {
        this.phimRepo = phimRepo;
        this.suatChieuRepo = suatChieuRepo;
        this.gheRepo = gheRepo;
        this.veRepo = veRepo;
        this.hoaDonRepo = hoaDonRepo;
        this.khachHangRepo = khachHangRepo;
        this.theRepo = theRepo;
        this.nvRepo = nvRepo;
    }

    // ---------- Buoc 2: tim & chon phim ----------
    @Transactional(readOnly = true)
    public List<PhimDto> timPhim(String tuKhoa) {
        String kw = tuKhoa == null ? "" : tuKhoa.trim();
        return phimRepo.timPhimDangChieu(kw, ThoiGian.homNay()).stream().map(DtoMapper::toDto).toList();
    }

    // ---------- Buoc 3: danh sach suat chieu ----------
    @Transactional(readOnly = true)
    public List<SuatChieuDto> layDanhSachSuatChieu(Long phimId) {
        if (!phimRepo.existsById(phimId)) {
            throw ApiException.notFound("Không tìm thấy phim");
        }
        return suatChieuRepo.timSuatSapChieu(phimId, ThoiGian.homNay(), ThoiGian.gioHienTai()).stream()
                .map(sc -> {
                    long tong = gheRepo.countByPhongChieuId(sc.getPhongChieu().getId());
                    long daBan = veRepo.countBySuatChieuId(sc.getId());
                    return new SuatChieuDto(sc.getId(), sc.getNgayChieu(), sc.getGioBatDau(), sc.getGioKetThuc(),
                            sc.getPhongChieu().getId(), sc.getPhongChieu().getTenPhong(), tong - daBan, tong);
                }).toList();
    }

    // ---------- Buoc 4: so do ghe ----------
    @Transactional(readOnly = true)
    public SoDoGheDto laySoDoGhe(Long suatChieuId) {
        SuatChieu sc = suatChieuRepo.findById(suatChieuId)
                .orElseThrow(() -> ApiException.notFound("Không tìm thấy suất chiếu"));
        Set<Long> daBan = new HashSet<>(veRepo.timIdGheDaBan(suatChieuId));
        List<GheDto> ghe = gheRepo.timSoDoGheTheoPhong(sc.getPhongChieu().getId()).stream()
                .map(g -> new GheDto(g.getId(), g.getMaGhe(), g.getHang(), g.getSoGhe(),
                        g.getLoaiGhe().getTenLoai(), g.getLoaiGhe().getDonGia(), daBan.contains(g.getId())))
                .toList();
        return new SoDoGheDto(sc.getId(), sc.getPhim().getTenPhim(), sc.getPhongChieu().getTenPhong(),
                sc.getNgayChieu(), sc.getGioBatDau(), sc.getGioKetThuc(), ghe);
    }

    // ---------- Buoc 5: nhan dien khach hang (tuy chon) ----------
    @Transactional(readOnly = true)
    public KhachHangTraCuuDto traCuuKhachHang(String maThe, String soDienThoai) {
        boolean coMa = maThe != null && !maThe.isBlank();
        boolean coSdt = soDienThoai != null && !soDienThoai.isBlank();
        if (coMa == coSdt) {
            throw ApiException.badRequest("Vui lòng nhập mã thẻ HOẶC số điện thoại để tra cứu");
        }
        if (coMa) {
            TheThanhVien the = theRepo.findByMaThe(maThe.trim().toUpperCase())
                    .orElseThrow(() -> ApiException.notFound("Mã thẻ thành viên không tồn tại"));
            return toTraCuu(the.getKhachHang(), the.getMaThe());
        }
        KhachHang kh = khachHangRepo.findBySoDienThoai(soDienThoai.replaceAll("\\s+", ""))
                .orElseThrow(() -> ApiException.notFound("Không tìm thấy khách hàng với số điện thoại này"));
        String ma = theRepo.findByKhachHangId(kh.getId()).map(TheThanhVien::getMaThe).orElse(null);
        return toTraCuu(kh, ma);
    }

    private KhachHangTraCuuDto toTraCuu(KhachHang kh, String maThe) {
        return new KhachHangTraCuuDto(kh.getId(), kh.getHoTen(), kh.getSoDienThoai(), kh.getEmail(),
                kh.getTrangThai(), maThe);
    }

    // ---------- Buoc 6: xac nhan & thanh toan (1 giao dich) ----------
    @Transactional
    public HoaDonDto thanhToan(Long nvId, ThanhToanRequest req) {
        String phuongThuc = req.phuongThucThanhToan();
        if (!HoaDon.PT_TIEN_MAT.equals(phuongThuc) && !HoaDon.PT_CHUYEN_KHOAN.equals(phuongThuc)) {
            throw ApiException.badRequest("Phương thức thanh toán chỉ có thể là Tiền mặt hoặc Chuyển khoản ngân hàng");
        }
        NVBanHang nv = nvRepo.findById(nvId)
                .orElseThrow(() -> ApiException.notFound("Không tìm thấy nhân viên bán hàng"));

        // Khoa suat chieu: cac giao dich ban ve cung suat chieu duoc xu ly tuan tu
        SuatChieu sc = suatChieuRepo.findByIdForUpdate(req.suatChieuId())
                .orElseThrow(() -> ApiException.notFound("Không tìm thấy suất chiếu"));
        if (!sc.thoiDiemBatDau().isAfter(ThoiGian.bayGio())) {
            throw ApiException.badRequest("Suất chiếu đã bắt đầu, không thể bán vé");
        }

        Set<Long> gheIds = new LinkedHashSet<>(req.gheIds());
        Map<Long, Ghe> gheMap = gheRepo.timTheoDanhSachId(gheIds).stream()
                .collect(Collectors.toMap(Ghe::getId, Function.identity()));
        if (gheMap.size() != gheIds.size()) {
            throw ApiException.notFound("Có ghế không tồn tại");
        }
        for (Ghe g : gheMap.values()) {
            if (!g.getPhongChieu().getId().equals(sc.getPhongChieu().getId())) {
                throw ApiException.badRequest("Ghế " + g.getMaGhe() + " không thuộc phòng chiếu của suất chiếu này");
            }
        }

        // Ghe chi duoc ban mot lan cho moi suat chieu
        Set<Long> daBan = new HashSet<>(veRepo.timIdGheDaBan(sc.getId()));
        List<String> trung = gheMap.values().stream().filter(g -> daBan.contains(g.getId()))
                .map(Ghe::getMaGhe).sorted().toList();
        if (!trung.isEmpty()) {
            throw ApiException.conflict("Ghế " + String.join(", ", trung)
                    + " vừa được bán cho giao dịch khác. Vui lòng chọn lại ghế.");
        }

        KhachHang khachHang = null; // NULL = khach khong dung the / khong cung cap thong tin
        if (req.khachHangId() != null) {
            khachHang = khachHangRepo.findById(req.khachHangId())
                    .orElseThrow(() -> ApiException.notFound("Không tìm thấy khách hàng"));
        }

        HoaDon hd = new HoaDon();
        hd.setNgayGioLap(ThoiGian.bayGio());
        hd.setLoai(HoaDon.LOAI_TAI_QUAY);
        hd.setNvBanHang(nv);
        hd.setKhachHang(khachHang);
        hd.setPhuongThucThanhToan(phuongThuc);

        long tong = 0;
        for (Ghe g : gheMap.values().stream()
                .sorted(Comparator.comparing(Ghe::getHang).thenComparing(Ghe::getSoGhe)).toList()) {
            Ve ve = new Ve();
            ve.setSuatChieu(sc);
            ve.setGhe(g);
            ve.setMaVe(Ve.taoMaVe(sc, g)); // ma ve: ngay xem + suat chieu + ghe + phim
            hd.themVe(ve);
            tong += g.getLoaiGhe().getDonGia(); // gia chi nam o loai ghe, khong luu tren ve
        }
        hd.setTongTien(tong);

        try {
            hoaDonRepo.saveAndFlush(hd); // cascade luu cac Ve
        } catch (DataIntegrityViolationException e) { // UNIQUE (suat_chieu_id, ghe_id) chan ban trung
            throw ApiException.conflict("Có ghế vừa được bán cho giao dịch khác. Vui lòng chọn lại ghế.");
        }

        String maThe = khachHang == null ? null
                : theRepo.findByKhachHangId(khachHang.getId()).map(TheThanhVien::getMaThe).orElse(null);
        return DtoMapper.toDto(hd, maThe);
    }
}
