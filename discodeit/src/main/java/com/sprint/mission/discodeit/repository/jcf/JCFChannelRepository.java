package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;


import java.util.*;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
@Repository
public class JCFChannelRepository implements ChannelRepository {
    private final Map<UUID, Channel> store = new HashMap<>();

    @Override
    public Channel save(Channel channel) {
        store.put(channel.getChannelId(),channel);
        return channel;
    }

    @Override
    public Optional<Channel> findByChannelId(UUID channelId) {
        return Optional.ofNullable(store.get(channelId));
    }

    @Override
    public List<Channel> findAllChannel() {
        return new ArrayList<>(store.values());
    }

    @Override
    public List<Channel> findAllByUserId(UUID userId) {
        return new ArrayList<>(store.values());
    }

    @Override
    public void deleteById(UUID channelId) {
        store.remove(channelId);
    }

    @Override
    public boolean existsById(UUID channelId) {
        return store.containsKey(channelId);
    }
}
