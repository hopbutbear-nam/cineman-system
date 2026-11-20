package vn.cineman.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.cineman.entity.KhachHang;

public interface KhachHangRepository extends JpaRepository<KhachHang, Long> {
    Optional<KhachHang> findBySoDienThoai(String soDienThoai);
}
