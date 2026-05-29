package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.BinaryContentUploadStatus;
import com.sprint.mission.discodeit.exception.binarycontent.BinaryContentNotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

// BinaryContentEventListener.java 수정
@Slf4j
@Component
@RequiredArgsConstructor
public class BinaryContentEventListener {

  // @Transactional 제거! 트랜잭션은 BinaryContentStorageService에서 처리
  private final BinaryContentStorageService binaryContentStorageService;

  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handleBinaryContentCreated(BinaryContentCreatedEvent event) {
    log.info("🔥 1. 리스너 시작: id={}", event.binaryContentId());
    // 트랜잭션이 필요한 로직은 별도 서비스로 위임
    binaryContentStorageService.storeAndUpdateStatus(
        event.binaryContentId(),
        event.bytes()
    );
  }
}

