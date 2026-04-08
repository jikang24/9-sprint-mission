package com.sprint.mission.discodeit.exception;

public enum ErrorCode {
  // User 관련
  USER_NOT_FOUND("사용자를 찾을 수 없습니다"),
  DUPLICATE_USER("이미 존재하는 사용자입니다"),

  // Channel 관련
  CHANNEL_NOT_FOUND("채널을 찾을 수 없습니다"),
  PRIVATE_CHANNEL_UPDATE("Private 채널은 수정할 수 없습니다");

  private final String message;  // 각 에러코드에 대응하는 메시지

  ErrorCode(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }
}