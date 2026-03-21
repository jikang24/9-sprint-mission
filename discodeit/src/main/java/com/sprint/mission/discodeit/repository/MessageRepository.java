package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MessageRepository extends JpaRepository<Message, UUID> {

  //N+1 방지 위한 @EntityGraph
  @EntityGraph(attributePaths = {
      "author",
      "author.status",
      "author.profile",
      "attachments",
      "channel"
  })
  List<Message> findAllByChannelIdOrderByCreatedAtDesc(UUID channelId, Pageable pageable);

  @EntityGraph(attributePaths = {
      "author",
      "author.status",
      "author.profile",
      "attachments",
      "channel"
  })
  List<Message> findAllByChannelIdAndCreatedAtBeforeOrderByCreatedAtDesc(
      UUID channelId,
      Instant cursor,
      Pageable pageable
  );

  Optional<Message> findTopByChannelIdOrderByCreatedAtDescIdDesc(UUID channelId);

  void deleteAllByChannelId(UUID channelId);

}
