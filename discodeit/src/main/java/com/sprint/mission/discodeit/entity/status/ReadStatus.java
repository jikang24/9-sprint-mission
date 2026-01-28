package com.sprint.mission.discodeit.entity.status;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Getter
public class ReadStatus {
    private final UUID messageId;
    private final UUID userId;
    private final UUID channelId;
    private final Long createdAt;
    private Long updatedAt;
    private boolean read;

    @Builder
    public ReadStatus(UUID messageId, UUID userId, UUID channelId) {
        this.messageId = messageId;
        this.userId = userId;
        this.channelId = channelId;
        this.createdAt = Instant.now().toEpochMilli();
        this.updatedAt = this.createdAt;
//        this.read = false;
    }
}
