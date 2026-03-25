package com.sprint.mission.discodeit.dto.response;

import java.time.Instant;
import java.util.Map;

public record ErrorResponse(
    Instant timestamp,      // 예외 발생 시각
    String code,            // 에러 코드명 (예: USER_NOT_FOUND)
    String message,         // 에러 메시지
    Map<String, Object> details,  // 추가 정보
    String exceptionType,   // 발생한 예외의 클래스 이름
    int status              // HTTP 상태코드
) {

}