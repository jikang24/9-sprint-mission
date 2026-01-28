package com.sprint.mission.discodeit.DTO;

import com.sprint.mission.discodeit.entity.Message;

import java.util.UUID;

public record MessageDTO (String text, UUID channelId, UUID authorId) {
    public Message toEntity(){
        return new Message(channelId,authorId,text);
    }

}
