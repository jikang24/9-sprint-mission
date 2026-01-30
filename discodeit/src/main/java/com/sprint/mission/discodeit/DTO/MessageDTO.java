package com.sprint.mission.discodeit.DTO;

import com.sprint.mission.discodeit.entity.Message;

import java.util.UUID;

public class MessageDTO{

    public record CreateMessageDTO (
            String text,
            UUID channelId,
            UUID authorId
    ) {
        public Message toEntity(){
            return new Message(authorId, channelId, text);
        }
    }

    public record UpdateMessageDTO (
            String text
    ) {
        public Message toEntity(){
            return null;
        }
    }
}
