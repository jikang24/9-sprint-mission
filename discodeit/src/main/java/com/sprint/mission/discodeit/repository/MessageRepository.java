package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, UUID> {

  //N+1 방지 위한 @EntityGraph
  @EntityGraph(attributePaths = {
      "author",
      "author.status",
      "author.profile",
      "attachments",
      "channel"
  })
  List<Message> findAllByChannelId(UUID channelId);

  void deleteAllByChannelId(UUID channelId);

  //페이지 관련 추가
  @EntityGraph(attributePaths = {
      "author",
      "author.status",
      "author.profile",
      "attachments",
      "channel"
  })
  Slice<Message> findAllByChannelIdOrderByCreatedAtDesc(UUID channelId, Pageable pageable);
}
