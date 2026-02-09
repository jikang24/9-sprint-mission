package com.sprint.mission.discodeit.dto;

import java.util.UUID;

public class UserServiceResponseDTO {

    public record FindUserId(
            UUID userId,
            boolean isOnline,
            Long lastUpdatedAt
    ){}
}
