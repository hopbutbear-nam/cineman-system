package vn.cineman.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

/** Thoi gian theo mui gio Viet Nam, dung chung cho toan he thong. */
public final class ThoiGian {
    public static final ZoneId MUI_GIO = ZoneId.of("Asia/Ho_Chi_Minh");

    private ThoiGian() {
    }

    public static LocalDateTime bayGio() {
        return LocalDateTime.now(MUI_GIO);
    }

    public static LocalDate homNay() {
        return LocalDate.now(MUI_GIO);
    }

    public static LocalTime gioHienTai() {
        return LocalTime.now(MUI_GIO);
    }
}
