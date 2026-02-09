package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.status.ReadStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReadStatusRepository {
    ReadStatus read (User userId, Channel channelId);

    Optional<ReadStatus> findByUserIdAndChannelId(UUID userId, UUID channelId);

    Optional<ReadStatus> findById(UUID id);

    List<ReadStatus> findAllByChannelId(UUID channelId);

    List<ReadStatus> findAllByUserId(UUID userId);

    ReadStatus update(ReadStatus readStatus);

    ReadStatus save(ReadStatus readStatus);

    ReadStatus delete(ReadStatus readStatus);


    boolean existsById(UUID messageId);

    void deleteById(UUID id);

    void deleteAllByChannelId(UUID channelId);
}
