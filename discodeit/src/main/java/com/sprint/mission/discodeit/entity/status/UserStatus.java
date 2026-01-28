package com.sprint.mission.discodeit.entity.status;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

public class UserStatus {
    @Getter
    private final UUID userId;
    private final Long cratedAt;
    private Long updatedAt;
    private String userName;



    public UserStatus(UUID userId, Long cratedAt) {
        this.userId = UUID.randomUUID();
        this.cratedAt = Instant.now().toEpochMilli();
        this.updatedAt = this.cratedAt;
        this.userName = "";

    }





}
