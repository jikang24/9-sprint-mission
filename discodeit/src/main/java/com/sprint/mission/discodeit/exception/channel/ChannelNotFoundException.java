package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.detail.ExceptionDetail;


public class ChannelNotFoundException extends ChannelException {

  public ChannelNotFoundException(ExceptionDetail details) {
    super(ErrorCode.CHANNEL_NOT_FOUND, details);
  }
}
