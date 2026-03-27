package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Slice;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@EnableJpaAuditing
class MessageRepositoryTest {

  @Autowired
  private MessageRepository messageRepository;

  @Autowired
  private TestEntityManager em;

  @Test
  @DisplayName("채널과 유저를 함께 조회 - 데이터 있음")
  void findAllByChannelIdWithAuthor_success() {
    User user = new User("testuser", "test@test.com", "password", null);
    em.persist(user);
    em.persist(new UserStatus(user, Instant.now()));

    Channel channel = new Channel(ChannelType.PUBLIC, "testchannel", "testdescription");
    em.persist(channel);

    Message message = new Message("testmessage", channel, user, List.of());
    em.persist(message);

    em.flush();

    Instant cursor = Instant.now().plusSeconds(10);
    PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Direction.DESC, "createdAt"));
    Slice<Message> result = messageRepository.findAllByChannelIdWithAuthor(
        channel.getId(), cursor, pageRequest);

    assertThat(result.getContent()).hasSize(1);
    assertThat(result.getContent().get(0).getContent()).isEqualTo("testmessage");
    assertThat(result.getContent().get(0).getAuthor().getUsername()).isEqualTo("testuser");
  }

  @Test
  @DisplayName("채널과 유저를 함께 조회 - 데이터 없음")
  void findAllByChannelIdWithAuthor_empty() {
    Channel channel = new Channel(ChannelType.PUBLIC, "testchannel", "testdescription");
    em.persist(channel);
    em.flush();

    Instant cursor = Instant.now().plusSeconds(10);
    PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Direction.DESC, "createdAt"));
    Slice<Message> result = messageRepository.findAllByChannelIdWithAuthor(
        channel.getId(), cursor, pageRequest);

    assertThat(result.getContent()).isEmpty();
    assertThat(result.hasNext()).isFalse();
    assertThat(result.hasPrevious()).isFalse();
  }

  @Test
  @DisplayName("채널 ID로 메시지 목록 조회 실패 - 채널 없음")
  void findAllByChannelId_fail_unknownChannel() {
    UUID channelId = UUID.randomUUID();
    PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Direction.DESC, "createdAt"));

    Slice<Message> result = messageRepository.findAllByChannelIdWithAuthor(channelId, Instant.now(),
        pageRequest);

    assertThat(result.getContent()).isEmpty();
  }

  @Test
  @DisplayName("마지막 메시지를 통한 채널 조회")
  void findLastMessageAtByChannelId_success() {
    User user = new User("testuser", "test@test.com", "password", null);
    em.persist(user);

    Channel channel = new Channel(ChannelType.PUBLIC, "testchannel", "testdescription");
    em.persist(channel);

    Message lastMessage = new Message("testmessage", channel, user, List.of());
    em.persist(lastMessage);

    em.flush();

    Optional<Instant> result = messageRepository.findLastMessageAtByChannelId(channel.getId());

    assertThat(result).isPresent();
    assertThat(result.get()).isBeforeOrEqualTo(Instant.now());
  }

  @Test
  @DisplayName("마지막 메시지를 통한 채널 조회 - 데이터 없음")
  void findLastMesssageAtByChannelId_empty() {

    Channel channel = new Channel(ChannelType.PUBLIC, "testchannel", "testdescription");
    em.persist(channel);
    em.flush();

    Optional<Instant> result = messageRepository.findLastMessageAtByChannelId(channel.getId());

    assertThat(result).isEmpty();
  }


}
