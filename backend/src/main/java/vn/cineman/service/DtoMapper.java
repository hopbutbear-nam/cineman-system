package vn.cineman.service;

import java.util.Comparator;
import java.util.List;
import vn.cineman.dto.HoaDonDto;
import vn.cineman.dto.NguoiDungDto;
import vn.cineman.dto.PhimDto;
import vn.cineman.dto.VeDto;
import vn.cineman.entity.HoaDon;
import vn.cineman.entity.KhachHang;
import vn.cineman.entity.NguoiDung;
import vn.cineman.entity.NhanVien;
import vn.cineman.entity.Phim;
import vn.cineman.entity.Ve;

/** Chuyen entity -> DTO. Goi trong ngu canh @Transactional (quan he LAZY). */
final class DtoMapper {
    private DtoMapper() {
    }

    static NguoiDungDto toDto(NguoiDung nd) {
        String chucVu = nd instanceof NhanVien nv ? nv.getChucVu() : null;
        String trangThai = nd instanceof KhachHang kh ? kh.getTrangThai() : null;
        return new NguoiDungDto(nd.getId(), nd.getTenDangNhap(), nd.getHoTen(), nd.getVaiTro(), nd.getDiaChi(),
                nd.getNgaySinh(), nd.getGioiTinh(), nd.getEmail(), nd.getSoDienThoai(), chucVu, trangThai);
    }

    static PhimDto toDto(Phim p) {
        return new PhimDto(p.getId(), p.getTenPhim(), p.getTheLoai(), p.getDaoDien(), p.getDienVien(),
                p.getThoiLuong(), p.getGioiHanDoTuoi(), p.getNgayKhoiChieu(), p.getNgayKetThuc(),
                p.getMoTa(), p.getPosterUrl());
    }

    static VeDto toDto(Ve v) {
        var sc = v.getSuatChieu();
        return new VeDto(v.getId(), v.getMaVe(), sc.getPhim().getTenPhim(), sc.getPhongChieu().getTenPhong(), sc.getNgayChieu(),
                sc.getGioBatDau(), sc.getGioKetThuc(), v.getGhe().getMaGhe(), v.getGhe().getLoaiGhe().getTenLoai(),
                v.getGiaVe());
    }

    static HoaDonDto toDto(HoaDon hd, String maThe) {
        List<VeDto> ves = hd.getDanhSachVe().stream()
                .sorted(Comparator.comparing((Ve v) -> v.getGhe().getHang()).thenComparing(v -> v.getGhe().getSoGhe()))
                .map(DtoMapper::toDto)
                .toList();
        return new HoaDonDto(hd.getId(), hd.getNgayGioLap(), hd.getLoai(), hd.getPhuongThucThanhToan(),
                hd.getTongTien(),
                hd.getNvBanHang() != null ? hd.getNvBanHang().getHoTen() : null,
                hd.getKhachHang() != null ? hd.getKhachHang().getHoTen() : null,
                maThe, ves);
    }
}
