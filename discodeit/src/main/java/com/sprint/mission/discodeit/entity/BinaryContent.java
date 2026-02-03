package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;


@Getter
public class BinaryContent {
    /** 바이너리 컨텐츠 식별자 */
    private final UUID profileId;
    private final List<UUID> attachmentIds;

    private final byte[] data;
    /** MIME 타입 (image/png, application/pdf 등) */
    private final String contentType;
    /** 데이터 크기 (bytes) */
    private final long size;
    private final long createdAt;

    public BinaryContent(byte[] data, String contentType) {
        this.data = data;
        this.contentType = contentType;
        this.size = data.length;
        this.createdAt = Instant.now().toEpochMilli();

        this.profileId = getProfileId();
        this.attachmentIds = getAttachmentIds();
    }



}
