// 새 파일: BinaryContentStatusUpdater.java
package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.BinaryContentUploadStatus;
import com.sprint.mission.discodeit.exception.binarycontent.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BinaryContentStorageService {

  private final BinaryContentStorage binaryContentStorage;
  private final BinaryContentRepository binaryContentRepository;

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void storeAndUpdateStatus(UUID binaryContentId, byte[] bytes) {
    log.info("🔥 2. DB 조회 시작");
    
    BinaryContent binaryContent = binaryContentRepository.findById(binaryContentId)
        .orElseThrow(() -> BinaryContentNotFoundException.withId(binaryContentId));

    log.info("🔥 3. DB 조회 완료: status={}", binaryContent.getStatus());

    try {
      binaryContentStorage.put(binaryContentId, bytes);
      binaryContent.updateStatus(BinaryContentUploadStatus.SUCCESS);
      log.info("🔥 5. SUCCESS");
    } catch (Exception e) {
      log.error("🔥 예외: {} - {}", e.getClass().getName(), e.getMessage(), e);
      binaryContent.updateStatus(BinaryContentUploadStatus.FAIL);
    }
  }
}