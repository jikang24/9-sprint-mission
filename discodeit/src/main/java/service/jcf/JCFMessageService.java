package service.jcf;

import entity.Message;
import entity.User;
import service.MessageService;

import java.nio.channels.Channel;
import java.util.*;

public class JCFMessageService implements MessageService {
    private List<Message> data = new ArrayList<>();


    @Override
    public Message create(String text, UUID channelId, UUID authorId) {
        Message message = new Message(authorId, channelId, text);
        data.add(message);
        return message;
    }

    @Override
    public Message find(UUID messageId) {
        for (Message message : data) {
            if (message.getMessageId().equals(messageId)) {
                return message;
            }
        }
        throw new RuntimeException("Message not found");
    }

    @Override
    public List<Message> findByChannelId(UUID channelId) {
        if (data.isEmpty()) {
            System.out.println("해당하는 채널이 없습니다.");
        }
        return data;
    }

    @Override
    public List<Message> findAll() {
        if (data.isEmpty()) {
            System.out.println("메시지가 없습니다.");
        }
        return data;
    }

    @Override
    public Message update(UUID messageId, String text) {
        for (Message message : data) {
            if (message.getMessageId().equals(messageId)){
                message.updateMessage(text);
                return message;
            }
        }
        if (find(messageId) == null) {
            throw new RuntimeException("해당 ID의 메시지가 없습니다.");
        }
        throw new RuntimeException("수정할 수 없습니다.");
    }

    @Override
    public boolean deleteMessage(UUID messageId) {
        Message message = find(messageId);
        if (message == null) {
            System.out.println("삭제할 메시지가 없습니다.");
            return false;
        }
        data.remove(message);
        return true;
    }

    @Override
    public void sendMessage(Message message) {
        List<String> user1 = new ArrayList<>();
        if (user1 != null) {

            System.out.println(user1.get(0));
        }
    }
// 심화단계



}
