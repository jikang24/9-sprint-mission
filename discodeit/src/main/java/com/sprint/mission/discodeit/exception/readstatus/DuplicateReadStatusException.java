package com.sprint.mission.discodeit.exception.readstatus;

import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.detail.ExceptionDetail;
import java.util.UUID;

public class DuplicateReadStatusException extends ReadStatusException {

  public DuplicateReadStatusException(ExceptionDetail details) {
    super(ErrorCode.DUPLICATE_READ_STATUS, details);
  }
} 