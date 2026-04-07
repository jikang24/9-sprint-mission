package com.sprint.mission.discodeit.exception.userstatus;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.detail.ExceptionDetail;

public class UserStatusException extends DiscodeitException {

  public UserStatusException(ErrorCode errorCode, ExceptionDetail details) {
    super(errorCode, details);
  }
  
} 