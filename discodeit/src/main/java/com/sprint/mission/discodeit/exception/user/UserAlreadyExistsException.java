package com.sprint.mission.discodeit.exception.user;

import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.detail.ExceptionDetail;

public class UserAlreadyExistsException extends UserException {

  public UserAlreadyExistsException(ExceptionDetail details) {
    super(ErrorCode.DUPLICATE_USER, details);
  }
}
