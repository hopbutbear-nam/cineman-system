package vn.cineman.repository;

import jakarta.persistence.LockModeType;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vn.cineman.entity.SuatChieu;

public interface SuatChieuRepository extends JpaRepository<SuatChieu, Long> {
    /** Cac suat chieu chua bat dau cua mot phim. */
    @Query("""
            select s from SuatChieu s join fetch s.phongChieu
            where s.phim.id = :phimId
              and (s.ngayChieu > :homNay or (s.ngayChieu = :homNay and s.gioBatDau > :gioHienTai))
            order by s.ngayChieu, s.gioBatDau
            """)
    List<SuatChieu> timSuatSapChieu(@Param("phimId") Long phimId,
                                    @Param("homNay") LocalDate homNay,
                                    @Param("gioHienTai") LocalTime gioHienTai);

    /** Khoa hang suat chieu (SELECT ... FOR UPDATE) de tuan tu hoa cac giao dich ban ve cung suat. */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from SuatChieu s where s.id = :id")
    Optional<SuatChieu> findByIdForUpdate(@Param("id") Long id);
}
