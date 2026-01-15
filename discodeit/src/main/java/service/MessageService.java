package service;

import entity.Message;
import entity.User;

import java.nio.channels.Channel;
import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message create(String text, UUID channelId, UUID authorId);

    Message find(UUID messageId);
    List<Message> findByChannelId(UUID channelId);

    List<Message> findAll();

    Message update(UUID messageId, String text);

    boolean deleteMessage(UUID messageId);

    // 심화단계

    void sendMessage(Message message);





}
