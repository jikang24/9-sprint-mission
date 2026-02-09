package com.sprint.mission.discodeit.dto;

import java.util.UUID;

public class UserStatusDTO {

    public record CreateUserStatusDTO(UUID userId) {
    }
    public record UpdateUserStatusDTO(UUID userId) {
    }

}
