package com.sprint.mission.discodeit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.PrivateChannelUpdateException;
import com.sprint.mission.discodeit.fixture.ChannelFixture;
import com.sprint.mission.discodeit.fixture.UserFixture;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)  // Mockito를 JUnit5에서 사용할 수 있게 활성화
class ChannelServiceTest {

  @InjectMocks
  private BasicChannelService channelService;

  @Mock
  private ChannelRepository channelRepository;
  @Mock
  private ReadStatusRepository readStatusRepository;
  @Mock
  private MessageRepository messageRepository;
  @Mock
  private UserRepository userRepository;
  @Mock
  private ChannelMapper channelMapper;

  @Test
  @DisplayName("공용 채널 생성 성공")
  void createPublicChannel_success() {
    PublicChannelCreateRequest request = ChannelFixture.createPublicChannelCreateRequest();
    Channel mockChannel = ChannelFixture.createPublicChannel();
    ChannelDto mockChannelDto = ChannelFixture.createPublicChannelDto();

    given(channelRepository.save(any(Channel.class))).willReturn(mockChannel);
    given(channelMapper.toDto(any(Channel.class))).willReturn(mockChannelDto);

    ChannelDto result = channelService.create(request);

    assertThat(result.type()).isEqualTo(ChannelType.PUBLIC);
    assertThat(result.name()).isEqualTo("testChannel");
    assertThat(result.description()).isEqualTo("testDescription");
    then(channelRepository).should().save(any(Channel.class));
  }

  @Test
  @DisplayName("사설 채널 생성 성공")
  void createPrivateChannel_success() {
    PrivateChannelCreateRequest request = ChannelFixture.createPrivateChannelCreateRequest();
    Channel mockChannel = ChannelFixture.createPrivateChannel();
    List<User> mockUsers = List.of(
        new User("user1", "user1@test.com", "password", null),
        new User("user2", "user2@test.com", "password", null)
    );
    ChannelDto mockChannelDto = ChannelFixture.createPrivateChannelDto();

    given(channelRepository.save(any(Channel.class))).willReturn(mockChannel);
    given(channelMapper.toDto(any(Channel.class))).willReturn(mockChannelDto);
    given(userRepository.findAllById(any())).willReturn(mockUsers);

    ChannelDto result = channelService.create(request);

    assertThat(result.type()).isEqualTo(ChannelType.PRIVATE);

    then(channelRepository).should().save(any(Channel.class));
    then(readStatusRepository).should().saveAll(any());
  }

  @Test
  @DisplayName("채널 수정 성공")
  void updatePublicChannel_success() {
    UUID channelId = UUID.randomUUID();
    PublicChannelUpdateRequest request = ChannelFixture.createPublicChannelUpdateRequest();
    Channel mockChannel = ChannelFixture.createPublicChannel();
    ChannelDto mockChannelDto = ChannelFixture.createUpdatedPublicChannelDto();

    given(channelRepository.findById(channelId)).willReturn(Optional.of(mockChannel));
    given(channelMapper.toDto(any(Channel.class))).willReturn(mockChannelDto);

    ChannelDto result = channelService.update(channelId, request);

    assertThat(result.name()).isEqualTo("newName");
    assertThat(result.description()).isEqualTo("newDescription");

    then(channelRepository).should().findById(channelId);
  }

  @Test
  @DisplayName("채널 수정 실패 - 채널 없음")
  void updateChannel_fail_notFound() {
    // given
    UUID channelId = UUID.randomUUID();
    PublicChannelUpdateRequest request = ChannelFixture.createPublicChannelUpdateRequest();

    // 채널이 존재하지 않는 상황
    given(channelRepository.findById(channelId)).willReturn(Optional.empty());

    // when & then
    assertThatThrownBy(() -> channelService.update(channelId, request))
        .isInstanceOf(ChannelNotFoundException.class);
  }

  @Test
  @DisplayName("채널 수정 실패 - Private 채널 수정 시도")
  void updateChannel_fail_privateChannel() {
    UUID channelId = UUID.randomUUID();
    PublicChannelUpdateRequest request = ChannelFixture.createPublicChannelUpdateRequest();
    // PRIVATE 채널이 존재하는 상황
    Channel mockChannel = ChannelFixture.createPrivateChannel();

    given(channelRepository.findById(channelId)).willReturn(Optional.of(mockChannel));

    // when & then
    assertThatThrownBy(() -> channelService.update(channelId, request))
        .isInstanceOf(PrivateChannelUpdateException.class);
  }

  @Test
  @DisplayName("채널 삭제 성공")
  void deleteChannel_success() {
    UUID channelId = UUID.randomUUID();

    given(channelRepository.existsById(channelId)).willReturn(true);

    channelService.delete(channelId);

    then(messageRepository).should().deleteAllByChannelId(channelId);
    then(readStatusRepository).should().deleteAllByChannelId(channelId);
    then(channelRepository).should().deleteById(channelId);
  }

  @Test
  @DisplayName("채널 삭제 실패 - 채널 없음")
  void deleteChannel_fail_notFound() {
    // given
    UUID channelId = UUID.randomUUID();

    // 채널이 존재하지 않는 상황
    given(channelRepository.existsById(channelId)).willReturn(false);

    // when & then
    assertThatThrownBy(() -> channelService.delete(channelId))
        .isInstanceOf(ChannelNotFoundException.class);

    // 채널이 없으니 실제 삭제는 호출되면 안 됨
    then(channelRepository).should(never()).deleteById(any());
  }

  @Test
  @DisplayName("유저 ID로 채널 목록 조회 성공")
  void findAllByUserId_success() {
    // given
    UUID userId = UUID.randomUUID();

    // 유저가 구독 중인 ReadStatus 목록 (PRIVATE 채널)
    Channel privateChannel = ChannelFixture.createPrivateChannel();
    User mockUser = UserFixture.createUser();
    ReadStatus mockReadStatus = new ReadStatus(mockUser, privateChannel, Instant.now());

    ChannelDto mockChannelDto = ChannelFixture.createPrivateChannelDto();

    given(readStatusRepository.findAllByUserId(userId)).willReturn(List.of(mockReadStatus));
    given(channelRepository.findAllByTypeOrIdIn(any(), any())).willReturn(List.of(privateChannel));
    given(channelMapper.toDto(any(Channel.class))).willReturn(mockChannelDto);

    // when
    List<ChannelDto> result = channelService.findAllByUserId(userId);

    // then
    assertThat(result).hasSize(1);
    assertThat(result.get(0).type()).isEqualTo(ChannelType.PRIVATE);
    then(readStatusRepository).should().findAllByUserId(userId);
    then(channelRepository).should().findAllByTypeOrIdIn(any(), any());
  }

  @Test
  @DisplayName("유저 ID로 채널 목록 조회 - 구독 채널 없음")
  void findAllByUserId_empty() {
    UUID userId = UUID.randomUUID();

    // 구독 중인 채널이 없는 상황
    given(readStatusRepository.findAllByUserId(userId)).willReturn(List.of());
    given(channelRepository.findAllByTypeOrIdIn(any(), any())).willReturn(List.of());

    List<ChannelDto> result = channelService.findAllByUserId(userId);

    // 예외 없이 빈 리스트 반환
    assertThat(result).isEmpty();
  }


}
