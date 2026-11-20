package vn.cineman.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.cineman.entity.NguoiDung;

public interface NguoiDungRepository extends JpaRepository<NguoiDung, Long> {
    Optional<NguoiDung> findByTenDangNhap(String tenDangNhap);

    /** Kiem tra so dien thoai da duoc nguoi dung KHAC su dung chua. */
    boolean existsBySoDienThoaiAndIdNot(String soDienThoai, Long id);
}
