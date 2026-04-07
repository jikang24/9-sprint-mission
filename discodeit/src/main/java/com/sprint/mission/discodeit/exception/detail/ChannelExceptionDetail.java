package com.sprint.mission.discodeit.exception.detail;

import lombok.Getter;

@Getter
public class ChannelExceptionDetail extends ExceptionDetail {

  private final String channelId;

  public ChannelExceptionDetail(String channelId) {
    this.channelId = channelId;
  }


  public static ChannelExceptionDetail ofChannelId(String channelId) {
    return new ChannelExceptionDetail(channelId);
  }
}