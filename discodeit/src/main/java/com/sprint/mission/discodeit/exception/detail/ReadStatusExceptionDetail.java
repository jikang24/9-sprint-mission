package com.sprint.mission.discodeit.exception.detail;

import lombok.Getter;

@Getter
public class ReadStatusExceptionDetail extends ExceptionDetail {

  private final String readStatusId;  // 조회 시도한 ReadStatus ID
  private final String userId;        // 중복된 ReadStatus의 userId
  private final String channelId;     // 중복된 ReadStatus의 channelId

  public ReadStatusExceptionDetail(String readStatusId, String userId, String channelId) {
    this.readStatusId = readStatusId;
    this.userId = userId;
    this.channelId = channelId;
  }

  // ReadStatus ID로 조회 실패했을 때
  public static ReadStatusExceptionDetail ofReadStatusId(String readStatusId) {
    return new ReadStatusExceptionDetail(readStatusId, null, null);
  }

  // userId + channelId 중복일 때
  public static ReadStatusExceptionDetail ofUserAndChannel(String userId, String channelId) {
    return new ReadStatusExceptionDetail(null, userId, channelId);
  }
}