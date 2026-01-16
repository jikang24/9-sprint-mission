package service;

import entity.Message;


import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message createMessage(String text, UUID channelId, UUID authorId);

    Message findByMessageId(UUID messageId);
    List<Message> findByChannelId(UUID channelId);

    List<Message> findAllMessage();

    Message updateMessage(UUID messageId, String text);

    boolean deleteMessage(UUID messageId);

    // 심화단계







}