package com.sprint.mission.discodeit.exception.detail;

import lombok.Getter;

@Getter
public class MessageExceptionDetail extends ExceptionDetail {

  private final String messageId;

  public MessageExceptionDetail(String messageId) {
    this.messageId = messageId;
  }

  // 메시지 ID로 조회 실패했을 때
  public static MessageExceptionDetail ofMessageId(String messageId) {
    return new MessageExceptionDetail(messageId);
  }
}