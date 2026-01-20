package repository.jcf;

import entity.Message;
import repository.MessageRepository;

import java.util.*;

public class JCFMessageRepository implements MessageRepository {
    private final Map<UUID, Message> store = new HashMap<>();

    @Override
    public Message save(Message message) {
        store.put(message.getMessageId(),message);
        return message;
    }

    @Override
    public Optional<Message> findByMessageId(Optional<UUID> messageId) {
        return Optional.ofNullable
                (store.get(messageId.get()));
    }

    @Override
    public List<Message> findBySenderId(UUID userId) {
        return new ArrayList<>(store.values());
    }

    @Override
    public List<Message> findByChannelId(UUID channelId) {
        return new ArrayList<>(store.values());
    }

    @Override
    public List<Message> findAllMessage() {
        return new ArrayList<>(store.values());
    }

    @Override
    public void deleteById(UUID messageId) {
        store.remove(messageId);
    }

    @Override
    public boolean existsById(UUID messageId) {
        return store.containsKey(messageId);
    }
}
