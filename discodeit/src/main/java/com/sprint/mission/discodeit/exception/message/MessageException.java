package com.sprint.mission.discodeit.exception.message;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.detail.ExceptionDetail;

public class MessageException extends DiscodeitException {

  public MessageException(ErrorCode errorCode, ExceptionDetail details) {
    super(errorCode, details);
  }


} 