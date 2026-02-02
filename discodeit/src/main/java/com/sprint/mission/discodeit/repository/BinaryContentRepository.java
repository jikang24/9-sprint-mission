package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.BinaryContent;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BinaryContentRepository {

    Optional<BinaryContent> findById(UUID id);

    BinaryContent save(BinaryContent binaryContent);

    void deleteById(UUID id);

}

