package com.sprint.mission.discodeit.DTO;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;

public record ChannelDTO (String channelName, ChannelType type, String description){
    public Channel toEntity(){
        return new Channel(channelName,description);
    }
}
