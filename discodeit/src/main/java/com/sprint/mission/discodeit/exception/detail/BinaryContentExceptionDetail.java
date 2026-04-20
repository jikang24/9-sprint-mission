package com.sprint.mission.discodeit.exception.detail;

import lombok.Getter;

@Getter
public class BinaryContentExceptionDetail extends ExceptionDetail {

  private final String binaryContentId;  // 조회 시도한 BinaryContent ID

  public BinaryContentExceptionDetail(String binaryContentId) {
    this.binaryContentId = binaryContentId;
  }

  // BinaryContent ID로 조회 실패했을 때
  public static BinaryContentExceptionDetail ofBinaryContentId(String binaryContentId) {
    return new BinaryContentExceptionDetail(binaryContentId);
  }
}