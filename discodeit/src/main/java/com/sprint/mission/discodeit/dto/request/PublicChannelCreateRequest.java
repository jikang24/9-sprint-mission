package com.sprint.mission.discodeit.dto.request;

import com.sprint.mission.discodeit.entity.ChannelType;

public record PublicChannelCreateRequest(
        String name,
        String description
//        ChannelType channelType
) {
}
