package vn.cineman.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.cineman.entity.Ve;

public interface VeRepository extends JpaRepository<Ve, Long> {
    @Query("select v.ghe.id from Ve v where v.suatChieu.id = :suatChieuId")
    List<Long> timIdGheDaBan(@Param("suatChieuId") Long suatChieuId);

    long countBySuatChieuId(Long suatChieuId);
}
