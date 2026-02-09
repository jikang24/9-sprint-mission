package com.sprint.mission.discodeit.dto;

import java.util.UUID;

public class ReadStatusDTO {

    public record CreateReadStatusDTO(UUID messageId, UUID userId, UUID channelId) {
    }

    public record UpdateReadStatusDTO(UUID messageId, UUID userId, UUID channelId) {
    }


}
