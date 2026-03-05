package com.sprint.mission.discodeit.dto.data;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.UUID;

public record MessageDto(
    UUID id,
    String content,
    Channel channel,
    User author,
    List<BinaryContentDto> binaryContents
) {

}
