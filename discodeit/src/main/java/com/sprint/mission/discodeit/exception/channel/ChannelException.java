package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.detail.ExceptionDetail;


public class ChannelException extends DiscodeitException {

  public ChannelException(ErrorCode errorCode, ExceptionDetail details) {
    super(errorCode, details);
  }
}