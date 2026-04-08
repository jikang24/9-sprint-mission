package com.sprint.mission.discodeit.exception;

import com.sprint.mission.discodeit.dto.response.ErrorResponse;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(DiscodeitException.class)
  public ResponseEntity<ErrorResponse> handleDiscodeitException(DiscodeitException e) {
    log.warn("DiscodeitException - code: {}, message: {}, details: {}",
        e.getErrorCode(), e.getMessage(), e.getDetails());

    HttpStatus status = resolveHttpStatus(e.getErrorCode());

    ErrorResponse response = new ErrorResponse(
        e.getTimestamp(),
        e.getErrorCode().name(),  // 예: "USER_NOT_FOUND"
        e.getMessage(),           // 예: "사용자를 찾을 수 없습니다"
        e.getDetails(),// 예: {"userId": "123e4567-..."}
        e.getClass().getSimpleName(),
        status.value()
    );

    return ResponseEntity.status(status).body(response);
  }

  // 예상치 못한 예외 처리 (기존 Exception 핸들러 유지)
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception e) {
    log.error("Unexpected error: {}", e.getMessage(), e);

    ErrorResponse response = new ErrorResponse(
        java.time.Instant.now(),
        "INTERNAL_ERROR",
        e.getMessage(),
        null,
        e.getClass().getSimpleName(),
        HttpStatus.INTERNAL_SERVER_ERROR.value()
    );
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationException(
      MethodArgumentNotValidException e) {

    // 어떤 필드가 왜 실패했는지 details에 담기
    Map<String, Object> details = new HashMap<>();
    e.getBindingResult().getFieldErrors()
        .forEach(error -> details.put(error.getField(), error.getDefaultMessage()));

    log.warn("Validation failed - details: {}", details);

    ErrorResponse response = new ErrorResponse(
        Instant.now(),
        "VALIDATION_ERROR",
        "요청 데이터가 유효하지 않습니다",
        details,
        e.getClass().getSimpleName(),
        HttpStatus.BAD_REQUEST.value()
    );
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
  }

  // ErrorCode → HTTP 상태코드 매핑
  private HttpStatus resolveHttpStatus(ErrorCode errorCode) {
    return switch (errorCode) {
      case USER_NOT_FOUND, CHANNEL_NOT_FOUND -> HttpStatus.NOT_FOUND;          // 404
      case DUPLICATE_USER, PRIVATE_CHANNEL_UPDATE -> HttpStatus.BAD_REQUEST;   // 400
    };
  }
}
