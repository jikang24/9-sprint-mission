package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@EnableJpaAuditing
class ChannelRepositoryTest {

  @Autowired
  private ChannelRepository channelRepository;

  @Autowired
  private TestEntityManager em;

  @Test
  @DisplayName("PUBLIC 채널과 구독 중인 PRIVATE 채널 함께 조회")
  void findAllByTypeOrIdIn_success() {
    // given
    // PUBLIC 채널 생성
    Channel publicChannel = new Channel(ChannelType.PUBLIC, "publicChannel", "description");
    em.persist(publicChannel);

    // PRIVATE 채널 생성
    Channel privateChannel = new Channel(ChannelType.PRIVATE, null, null);
    em.persist(privateChannel);

    // 유저가 PRIVATE 채널을 구독 중인 상황
    User user = new User("testUser", "test@test.com", "password", null);
    em.persist(user);
    ReadStatus readStatus = new ReadStatus(user, privateChannel, Instant.now());
    em.persist(readStatus);

    em.flush();

    // 구독 중인 채널 ID 목록
    List<UUID> subscribedChannelIds = List.of(privateChannel.getId());

    // when
    List<Channel> result = channelRepository.findAllByTypeOrIdIn(
        ChannelType.PUBLIC, subscribedChannelIds
    );

    // then - PUBLIC + 구독 중인 PRIVATE 채널 모두 조회
    assertThat(result).hasSize(2);
    assertThat(result).extracting(Channel::getType)
        .containsExactlyInAnyOrder(ChannelType.PUBLIC, ChannelType.PRIVATE);
  }

  @Test
  @DisplayName("구독 채널 없을 때 PUBLIC 채널만 조회")
  void findAllByTypeOrIdIn_onlyPublic() {
    // given
    Channel publicChannel = new Channel(ChannelType.PUBLIC, "publicChannel", "description");
    em.persist(publicChannel);

    Channel privateChannel = new Channel(ChannelType.PRIVATE, null, null);
    em.persist(privateChannel);

    em.flush();

    // 구독 중인 채널 없음
    List<UUID> subscribedChannelIds = List.of();

    // when
    List<Channel> result = channelRepository.findAllByTypeOrIdIn(
        ChannelType.PUBLIC, subscribedChannelIds
    );

    // then - PUBLIC 채널만 조회
    assertThat(result).hasSize(1);
    assertThat(result.get(0).getType()).isEqualTo(ChannelType.PUBLIC);
  }

  @Test
  @DisplayName("아무 채널도 없을 때 빈 결과 반환")
  void findAllByTypeOrIdIn_empty() {
    // given - 아무 데이터 없음

    // when
    List<Channel> result = channelRepository.findAllByTypeOrIdIn(
        ChannelType.PUBLIC, List.of()
    );

    // then
    assertThat(result).isEmpty();
  }
}


