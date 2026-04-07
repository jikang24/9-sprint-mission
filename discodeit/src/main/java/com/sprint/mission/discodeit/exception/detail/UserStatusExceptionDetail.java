package com.sprint.mission.discodeit.exception.detail;

import lombok.Getter;

@Getter
public class UserStatusExceptionDetail extends ExceptionDetail {

  private final String userStatusId;  // 조회 시도한 UserStatus ID
  private final String userId;        // 중복된 UserStatus의 userId

  public UserStatusExceptionDetail(String userStatusId, String userId) {
    this.userStatusId = userStatusId;
    this.userId = userId;
  }

  // UserStatus ID로 조회 실패했을 때
  public static UserStatusExceptionDetail ofUserStatusId(String userStatusId) {
    return new UserStatusExceptionDetail(userStatusId, null);
  }

  // userId 중복일 때
  public static UserStatusExceptionDetail ofUserId(String userId) {
    return new UserStatusExceptionDetail(null, userId);
  }
}