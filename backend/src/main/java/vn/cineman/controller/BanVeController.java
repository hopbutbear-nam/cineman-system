package vn.cineman.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import vn.cineman.dto.HoaDonDto;
import vn.cineman.dto.KhachHangTraCuuDto;
import vn.cineman.dto.PhimDto;
import vn.cineman.dto.SoDoGheDto;
import vn.cineman.dto.SuatChieuDto;
import vn.cineman.dto.ThanhToanRequest;
import vn.cineman.security.PhienDangNhap;
import vn.cineman.security.VaiTro;
import vn.cineman.security.YeuCauVaiTro;
import vn.cineman.service.BanVeService;

/** Nghiep vu 2 - tac nhan chinh NVBanHang (khach hang la tac nhan gian tiep, khong goi API). */
@RestController
@RequestMapping("/api/ban-ve")
@YeuCauVaiTro(VaiTro.NV_BAN_HANG)
public class BanVeController {
    private final BanVeService banVeService;

    public BanVeController(BanVeService banVeService) {
        this.banVeService = banVeService;
    }

    @GetMapping("/phim")
    public List<PhimDto> timPhim(@RequestParam(defaultValue = "") String tuKhoa) {
        return banVeService.timPhim(tuKhoa);
    }

    @GetMapping("/phim/{phimId}/suat-chieu")
    public List<SuatChieuDto> suatChieuCuaPhim(@PathVariable Long phimId) {
        return banVeService.layDanhSachSuatChieu(phimId);
    }

    @GetMapping("/suat-chieu/{suatChieuId}/ghe")
    public SoDoGheDto soDoGhe(@PathVariable Long suatChieuId) {
        return banVeService.laySoDoGhe(suatChieuId);
    }

    /** Truyen DUNG MOT trong hai tham so: maThe hoac soDienThoai. */
    @GetMapping("/khach-hang")
    public KhachHangTraCuuDto traCuuKhachHang(@RequestParam(required = false) String maThe,
                                              @RequestParam(required = false) String soDienThoai) {
        return banVeService.traCuuKhachHang(maThe, soDienThoai);
    }

    @PostMapping("/thanh-toan")
    @ResponseStatus(HttpStatus.CREATED)
    public HoaDonDto thanhToan(PhienDangNhap phien, @Valid @RequestBody ThanhToanRequest req) {
        return banVeService.thanhToan(phien.nguoiDungId(), req);
    }
}
