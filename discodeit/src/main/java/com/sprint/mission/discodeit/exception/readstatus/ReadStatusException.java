package com.sprint.mission.discodeit.exception.readstatus;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.detail.ExceptionDetail;

public class ReadStatusException extends DiscodeitException {

  public ReadStatusException(ErrorCode errorCode, ExceptionDetail details) {
    super(errorCode, details);
  }
} 