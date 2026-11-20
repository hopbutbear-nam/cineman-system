package vn.cineman.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.cineman.entity.TheThanhVien;

public interface TheThanhVienRepository extends JpaRepository<TheThanhVien, Long> {
    Optional<TheThanhVien> findByMaThe(String maThe);

    Optional<TheThanhVien> findByKhachHangId(Long khachHangId);
}
