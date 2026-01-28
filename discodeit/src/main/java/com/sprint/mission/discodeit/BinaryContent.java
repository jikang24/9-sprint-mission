package com.sprint.mission.discodeit;

import lombok.Getter;

import java.util.UUID;

public class BinaryContent {
    @Getter
    private final UUID messageId;
    private final UUID userId;
    private final Long createdAt;
//    private byte[] content;
//    private String contentType;
//    private Long size;
//    private String fileName;


    public BinaryContent(UUID messageId, UUID userId, Long createdAt) {
        this.messageId = messageId;
        this.userId = userId;
        this.createdAt = createdAt;
    }




}
