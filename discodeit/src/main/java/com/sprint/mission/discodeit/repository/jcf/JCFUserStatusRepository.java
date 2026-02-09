package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.status.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
@Repository
public class JCFUserStatusRepository implements UserStatusRepository {
    private final Map<UUID, UserStatus> data;

    public JCFUserStatusRepository() {
        this.data = new HashMap<>();
    }

    @Override
    public UserStatus save(UserStatus userStatus) {
        this.data.put(userStatus.getId(), userStatus);
        return userStatus;
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
        return Optional.ofNullable(this.data.get(userId));
    }

    @Override
    public Optional<UserStatus> findAllByUserId(UUID userId) {
        return this.findAll().stream()
                .filter(userStatus -> userStatus.getUserId().equals(userId))
                .findFirst();
    }

    public List<UserStatus> findAll() {
        return this.data.values().stream().toList();
    }

    @Override
    public UserStatus update(UserStatus userStatus) {
        return null;
    }

    @Override
    public UserStatus updateByUserId(UUID userId, UserStatus userStatus) {
        return null;
    }

    @Override
    public boolean existsById(UUID userId) {
        return this.data.containsKey(userId);
    }

    @Override
    public void deleteById(UUID userId) {
        this.findByUserId(userId)
                .ifPresent(userStatus -> this.deleteByUserId(userStatus.getId()));

    }

    private void deleteByUserId(UUID userId) {
        this.data.remove(userId);
    }
}
