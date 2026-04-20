package com.sprint.mission.discodeit.exception.detail;

import lombok.Getter;

@Getter
public class UserExceptionDetail extends ExceptionDetail {

  private final String userId;
  private final String email;
  private final String username;

  public UserExceptionDetail(String userId, String email, String username) {
    this.userId = userId;
    this.email = email;
    this.username = username;
  }

  public static UserExceptionDetail ofUserId(String userId) {
    return new UserExceptionDetail(userId, null, null);
  }


  public static UserExceptionDetail ofEmail(String email) {
    return new UserExceptionDetail(null, email, null);
  }


  public static UserExceptionDetail ofUsername(String username) {
    return new UserExceptionDetail(null, null, username);
  }
}