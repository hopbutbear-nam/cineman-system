package vn.cineman.exception;

import java.util.LinkedHashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import vn.cineman.util.ThoiGian;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiError> xuLyApi(ApiException ex) {
        return tao(ex.getStatus(), ex.getMessage(), ex.getErrors());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> xuLyValidate(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new LinkedHashMap<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            errors.putIfAbsent(fe.getField(), fe.getDefaultMessage());
        }
        return tao(HttpStatus.BAD_REQUEST, "Dữ liệu không hợp lệ", errors);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class,
            MissingServletRequestParameterException.class})
    public ResponseEntity<ApiError> xuLyYeuCauSai(Exception ex) {
        return tao(HttpStatus.BAD_REQUEST, "Yêu cầu không hợp lệ (sai định dạng dữ liệu hoặc thiếu tham số)", Map.of());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> xuLyRangBuoc(DataIntegrityViolationException ex) {
        log.warn("Vi phạm ràng buộc dữ liệu: {}", ex.getMostSpecificCause().getMessage());
        return tao(HttpStatus.CONFLICT, "Dữ liệu bị trùng hoặc vi phạm ràng buộc, vui lòng kiểm tra lại", Map.of());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> xuLyKhac(Exception ex) {
        // Cac loi MVC chuan (404 tai nguyen, 405 ...) tu mang san ma HTTP
        if (ex instanceof org.springframework.web.ErrorResponse er) {
            return tao(er.getStatusCode(), "Yêu cầu không hợp lệ", Map.of());
        }
        log.error("Lỗi không mong muốn", ex);
        return tao(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi hệ thống, vui lòng thử lại sau", Map.of());
    }

    private ResponseEntity<ApiError> tao(HttpStatusCode status, String message, Map<String, String> errors) {
        return ResponseEntity.status(status)
                .body(new ApiError(ThoiGian.bayGio(), status.value(), message, errors));
    }
}
