package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.entity.BinaryContentUploadStatus;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BinaryContentStorageService {

  private final BinaryContentStorage binaryContentStorage;
  private final BinaryContentService binaryContentService;

  public void storeAndUpdateStatus(UUID binaryContentId, byte[] bytes) {
    log.info("바이너리 파일 저장 시작: id={}", binaryContentId);

    try {
      binaryContentStorage.put(binaryContentId, bytes);
      binaryContentService.updateStatus(binaryContentId, BinaryContentUploadStatus.SUCCESS);
      log.info("바이너리 파일 저장 성공: id={}", binaryContentId);

    } catch (Exception e) {
      log.error("바이너리 파일 저장 실패: id={}, error={}", binaryContentId, e.getMessage(), e);
      binaryContentService.updateStatus(binaryContentId, BinaryContentUploadStatus.FAIL);
    }
  }
}