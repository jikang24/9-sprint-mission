package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.status.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class FileReadStatusRepository implements ReadStatusRepository {
    @Override
    public ReadStatus read(User userId, Channel channelId) {
        return null;
    }

    @Override
    public Optional<ReadStatus> findByUserIdAndChannelId(UUID userId, UUID channelId) {
        return Optional.empty();
    }

    @Override
    public Optional<ReadStatus> findAllByUserId(UUID userId) {
        return Optional.empty();
    }

    @Override
    public ReadStatus update(ReadStatus readStatus) {
        return null;
    }

    @Override
    public ReadStatus save(ReadStatus readStatus) {
        return null;
    }

    @Override
    public ReadStatus delete(ReadStatus readStatus) {
        return null;
    }

    @Override
    public boolean existsById(UUID messageId) {
        return false;
    }
}
