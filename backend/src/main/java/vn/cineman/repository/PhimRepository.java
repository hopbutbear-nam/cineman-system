package vn.cineman.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.cineman.entity.Phim;

public interface PhimRepository extends JpaRepository<Phim, Long> {
    /** Phim con trong thoi gian chieu, khop tu khoa (rong = tat ca). */
    @Query("""
            select p from Phim p
            where p.ngayKetThuc >= :homNay
              and (:tuKhoa = '' or lower(p.tenPhim) like lower(concat('%', :tuKhoa, '%')))
            order by p.tenPhim
            """)
    List<Phim> timPhimDangChieu(@Param("tuKhoa") String tuKhoa, @Param("homNay") LocalDate homNay);
}
