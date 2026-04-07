package com.sprint.mission.discodeit.exception.userstatus;

import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.detail.ExceptionDetail;
import java.util.UUID;

public class DuplicateUserStatusException extends UserStatusException {

  public DuplicateUserStatusException(ExceptionDetail details) {
    super(ErrorCode.DUPLICATE_USER_STATUS, details);
  }
  
} 