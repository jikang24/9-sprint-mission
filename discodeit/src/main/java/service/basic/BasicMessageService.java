package service.basic;

import entity.Message;
import repository.ChannelRepository;
import repository.MessageRepository;
import repository.UserRepository;
import service.MessageService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    public BasicMessageService(MessageRepository messageRepository, UserRepository userRepository, ChannelRepository channelRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.channelRepository = channelRepository;
    }

    @Override
    public Message createMessage(String text, UUID channelId, UUID authorId) {
        Message message = new Message(authorId, channelId, text);
        return messageRepository.save(message);
    }

    @Override
    public Message findByMessageId(UUID messageId) {
        return messageRepository.findByMessageId(messageId)
                .orElseThrow(() -> new NoSuchElementException
                        ("Message with id " + messageId + "not found"));
    }

    public List<Message> findMessagesBySender(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("존재하지 않는 사용자");
        }
        return messageRepository.findBySenderId(userId);
    }

    @Override
    public List<Message> findByChannelId(UUID channelId) {
        if (!channelRepository.existsById(channelId)) {
            throw new IllegalArgumentException("존재하지 않는 채널");
        }
        return messageRepository.findByChannelId(channelId);
    }

    @Override
    public List<Message> findAllMessage() {
        return messageRepository.findAllMessage();
    }

    @Override
    public Message updateMessage(UUID messageId, String text) {
        Message message = messageRepository.findByMessageId(messageId)
                .orElseThrow(() -> new NoSuchElementException
                        ("Message with id " + messageId + "not found"));
        return messageRepository.save(message);
    }

    @Override
    public boolean deleteMessage(UUID messageId) {
        if (messageRepository.existsById(messageId)) {
            throw new NoSuchElementException
                    ("Message with id " + messageId + "not found");
        }
        messageRepository.deleteById(messageId);
        return true;
    }
}
