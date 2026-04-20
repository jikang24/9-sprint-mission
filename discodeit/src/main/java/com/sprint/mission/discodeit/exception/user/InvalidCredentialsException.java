package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.detail.ExceptionDetail;
import com.sprint.mission.discodeit.exception.detail.UserExceptionDetail;

public class InvalidCredentialsException extends UserException {

  public InvalidCredentialsException(ExceptionDetail details) {
    super(ErrorCode.INVALID_USER_CREDENTIALS, details);
  }

  public static InvalidCredentialsException wrongPassword() {
    return new InvalidCredentialsException(UserExceptionDetail.ofUsername(null));
  }


} 