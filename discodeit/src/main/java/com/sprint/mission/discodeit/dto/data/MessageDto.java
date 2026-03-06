package com.sprint.mission.discodeit.dto.data;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record MessageDto(
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    String content,
    UUID channel,
    UserDto author,
    List<BinaryContentDto> attachments
) {

}
