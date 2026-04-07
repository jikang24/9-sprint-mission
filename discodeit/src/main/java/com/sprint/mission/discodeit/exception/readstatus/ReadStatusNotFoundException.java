package com.sprint.mission.discodeit.exception.readstatus;

import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.detail.ExceptionDetail;
import java.util.UUID;

public class ReadStatusNotFoundException extends ReadStatusException {

  public ReadStatusNotFoundException(ExceptionDetail details) {
    super(ErrorCode.READ_STATUS_NOT_FOUND, details);
  }
} 