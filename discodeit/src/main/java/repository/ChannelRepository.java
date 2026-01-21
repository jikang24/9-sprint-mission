package repository;

import entity.Channel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelRepository {
    Channel save(Channel channel);

    Optional<Channel> findByChannelId(UUID channelId);

    List<Channel> findAllChannel();

    boolean existsById(UUID channelId);

    void deleteById(UUID channelId);



}
