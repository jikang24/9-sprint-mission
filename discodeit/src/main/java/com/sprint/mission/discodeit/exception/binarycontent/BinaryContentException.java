package com.sprint.mission.discodeit.exception.binarycontent;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.detail.ExceptionDetail;

public class BinaryContentException extends DiscodeitException {

  public BinaryContentException(ErrorCode errorCode, ExceptionDetail details) {
    super(errorCode, details);
  }
} 