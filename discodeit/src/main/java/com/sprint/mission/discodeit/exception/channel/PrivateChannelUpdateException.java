package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.detail.ExceptionDetail;


public class PrivateChannelUpdateException extends ChannelException {

  public PrivateChannelUpdateException(ExceptionDetail details) {
    super(ErrorCode.PRIVATE_CHANNEL_UPDATE, details);
  }
}
