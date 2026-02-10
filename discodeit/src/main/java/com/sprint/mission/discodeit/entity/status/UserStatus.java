package com.sprint.mission.discodeit.entity.status;

import lombok.Getter;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Getter
public class UserStatus implements Serializable {
    private static final long serialVersionUID = 1L;

//    private final UUID id;
//    private final Long createdAt;
//
//    private final UUID userId;
//
//    private Instant lastUpdatedAt;
//
//
//    public UserStatus(UUID userId, Instant lastUpdatedAt) {
//        this.id = UUID.randomUUID();
//        this.userId = userId;
//        this.createdAt = Instant.now().toEpochMilli();
//        this.lastUpdatedAt = lastUpdatedAt;
//
//    }
//
//
//
//
////마지막 접속 시간을 기준으로 현재 로그인한 유저로 판단할 수 있는 메소드
//    public boolean isOnline(){
//        long now = Instant.now().toEpochMilli();
//        long fiveMinutes = 5 * 60 * 1000L;
//        //5분, 60초, 1000ms
//        return now - lastUpdatedAt <= fiveMinutes;
//    }
//

private UUID id;
    private Instant createdAt;
    private Instant updatedAt;
    //
    private UUID userId;
    private Instant lastActiveAt;

    public UserStatus(UUID userId, Instant lastActiveAt) {
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        //
        this.userId = userId;
        this.lastActiveAt = lastActiveAt;
    }

    public void update(Instant lastActiveAt) {
        boolean anyValueUpdated = false;
        if (lastActiveAt != null && !lastActiveAt.equals(this.lastActiveAt)) {
            this.lastActiveAt = lastActiveAt;
            anyValueUpdated = true;
        }

        if (anyValueUpdated) {
            this.updatedAt = Instant.now();
        }
    }

    public Boolean isOnline() {
        Instant instantFiveMinutesAgo = Instant.now().minus(Duration.ofMinutes(5));

        return lastActiveAt.isAfter(instantFiveMinutesAgo);
    }


}
