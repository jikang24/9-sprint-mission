package com.sprint.mission.discodeit.DTO;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;

public class ChannelDTO {

    public record CreateChannelDTO (
            String channelName,
            ChannelType type,
            String description
    ) {
       public Channel toEntity(){
           return new Channel(channelName, description);
       }
    }

    public record UpdateChannelDTO (
            String channelName,
            ChannelType type,
            String description
    ) {
        public Channel toEntity(){
            return new Channel(channelName, description);
        }
    }

}
