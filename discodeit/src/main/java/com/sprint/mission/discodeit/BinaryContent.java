package com.sprint.mission.discodeit;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;


@Getter
public class BinaryContent {
    /** 바이너리 컨텐츠 식별자 */
    private final UUID id;
    /** 실제 바이너리 데이터 */
    private final byte[] data;
    /** MIME 타입 (image/png, application/pdf 등) */
    private final String contentType;
    /** 데이터 크기 (bytes) */
    private final long size;
    private final long createdAt;

    public BinaryContent(byte[] data, String contentType) {
        this.id = UUID.randomUUID();
        this.data = data;
        this.contentType = contentType;
        this.size = data.length;
        this.createdAt = Instant.now().toEpochMilli();
    }



}
