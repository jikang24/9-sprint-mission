package com.sprint.mission.discodeit.dto.data;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import java.time.Instant;
import java.util.UUID;

public record ReadStatusDto(
    UUID id,
    User user,
    Channel channel,
    Instant lastReadAt
) {

}
