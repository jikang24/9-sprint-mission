package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import java.util.Map;
import lombok.Getter;

@Getter
public class DiscodeitException extends RuntimeException {

  private final Instant timestamp = Instant.now();  // 예외 발생 시각
  private final ErrorCode errorCode;                // 어떤 종류의 에러인지
  private final Map<String, Object> details;        // 에러 발생 상황의 추가 정보 (어떤 ID, 어떤 값 때문인지)

  public DiscodeitException(ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode.getMessage());  // RuntimeException의 message로 ErrorCode의 메시지를 사용
    this.errorCode = errorCode;
    this.details = details;
  }
}