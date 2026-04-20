package com.sprint.mission.discodeit.exception.message;

import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.detail.ExceptionDetail;
import java.util.UUID;

public class MessageNotFoundException extends MessageException {

  public MessageNotFoundException(ExceptionDetail details) {
    super(ErrorCode.MESSAGE_NOT_FOUND, details);
  }


} 