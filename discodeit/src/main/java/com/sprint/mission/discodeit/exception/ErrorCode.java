package com.sprint.mission.discodeit.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
  // User 관련
  USER_NOT_FOUND("사용자를 찾을 수 없습니다", HttpStatus.NOT_FOUND),
  DUPLICATE_USER("이미 존재하는 사용자입니다", HttpStatus.BAD_REQUEST),
  INVALID_USER_CREDENTIALS("잘못된 사용자 인증 정보입니다.", HttpStatus.UNAUTHORIZED),

  // Channel 관련
  CHANNEL_NOT_FOUND("채널을 찾을 수 없습니다", HttpStatus.NOT_FOUND),
  PRIVATE_CHANNEL_UPDATE("Private 채널은 수정할 수 없습니다", HttpStatus.BAD_REQUEST),

  // Message 관련
  MESSAGE_NOT_FOUND("메시지를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

  // BinaryContent 관련
  BINARY_CONTENT_NOT_FOUND("바이너리 컨텐츠를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

  // ReadStatus 관련
  READ_STATUS_NOT_FOUND("읽음 상태를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
  DUPLICATE_READ_STATUS("이미 존재하는 읽음 상태입니다.", HttpStatus.BAD_REQUEST),

  // UserStatus 관련
  USER_STATUS_NOT_FOUND("사용자 상태를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
  DUPLICATE_USER_STATUS("이미 존재하는 사용자 상태입니다.", HttpStatus.BAD_REQUEST),

  // Server 관련
  INTERNAL_SERVER_ERROR("서버 내부 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
  INVALID_REQUEST("잘못된 요청입니다.", HttpStatus.BAD_REQUEST),

  // 요청 데이터 유효성 관련
  VALIDATION_ERROR("요청 데이터가 유효하지 않습니다.", HttpStatus.BAD_REQUEST);

  private final String message;
  private final HttpStatus httpStatus;  // ← 추가

  ErrorCode(String message, HttpStatus httpStatus) {
    this.message = message;
    this.httpStatus = httpStatus;
  }
}