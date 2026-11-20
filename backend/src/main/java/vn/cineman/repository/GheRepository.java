package vn.cineman.repository;

import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.cineman.entity.Ghe;

public interface GheRepository extends JpaRepository<Ghe, Long> {
    @Query("select g from Ghe g join fetch g.loaiGhe where g.phongChieu.id = :phongId order by g.hang, g.soGhe")
    List<Ghe> timSoDoGheTheoPhong(@Param("phongId") Long phongId);

    @Query("select g from Ghe g join fetch g.loaiGhe join fetch g.phongChieu where g.id in :ids")
    List<Ghe> timTheoDanhSachId(@Param("ids") Collection<Long> ids);

    long countByPhongChieuId(Long phongChieuId);
}
