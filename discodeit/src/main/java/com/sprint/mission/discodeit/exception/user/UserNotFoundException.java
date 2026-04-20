package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.detail.ExceptionDetail;

public class UserNotFoundException extends UserException {

  public UserNotFoundException(ExceptionDetail details) {
    super(ErrorCode.USER_NOT_FOUND, details);
  }
}
