package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class FileBinaryContentRepository implements BinaryContentRepository {
    @Override
    public BinaryContent create(BinaryContent binaryContent) {
        return null;
    }

    @Override
    public Optional<BinaryContent> findById(UUID id) {
        return Optional.empty();
    }

    @Override
    public Optional<BinaryContent> findAllByIdIn(Iterable<UUID> ids) {
        return Optional.empty();
    }

    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        return null;
    }

    @Override
    public void deleteById(UUID id) {

    }
}
