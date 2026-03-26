package com.sprint.mission.discodeit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

  @InjectMocks
  private BasicMessageService messageService;

  @Mock
  private MessageRepository messageRepository;
  @Mock
  private ChannelRepository channelRepository;
  @Mock
  private MessageMapper messageMapper;
  @Mock
  private BinaryContentStorage binaryContentStorage;
  @Mock
  private BinaryContentRepository binaryContentRepository;
  @Mock
  private UserRepository userRepository;
  @Mock
  private PageResponseMapper pageResponseMapper;

  @Test
  @DisplayName("메시지 생성 성공")
  void createMessage_success() {
    UUID channelId = UUID.randomUUID();
    UUID authorId = UUID.randomUUID();

    MessageCreateRequest request = new MessageCreateRequest("testContent", channelId, authorId);
    Channel mockChannel = new Channel(ChannelType.PUBLIC, "testChannel", "testDescription");
    User mockAuthor = new User("testUser", "test@test.com", "password123", null);
    Message mockMessage = new Message("testContent", mockChannel, mockAuthor, List.of());
    UserDto mockAuthorDto = new UserDto(authorId, "testUser", "test@test.com", null, false);
    MessageDto mockMessageDto = new MessageDto(
        UUID.randomUUID(),
        Instant.now(),
        null,
        "testContent",
        channelId,
        mockAuthorDto,
        List.of()
    );

    given(channelRepository.findById(channelId)).willReturn(Optional.of(mockChannel));
    given(userRepository.findById(authorId)).willReturn(Optional.of(mockAuthor));
    given(messageRepository.save(any(Message.class))).willReturn(mockMessage);
    given(messageMapper.toDto(any(Message.class))).willReturn(mockMessageDto);

    MessageDto result = messageService.create(request, List.of());

    assertThat(result.content()).isEqualTo("testContent");
    assertThat(result.channelId()).isEqualTo(channelId);
    assertThat(result.author().id()).isEqualTo(authorId);

    then(messageRepository).should().save(any(Message.class));
  }

  @Test
  @DisplayName("메시지 생성 실패 - 채널 없음")
  void createMessage_fail_channelNotFound() {
    UUID channelId = UUID.randomUUID();
    UUID authorId = UUID.randomUUID();
    MessageCreateRequest request = new MessageCreateRequest("testContent", channelId, authorId);

    given(channelRepository.findById(channelId)).willReturn(Optional.empty());

    assertThatThrownBy(() -> messageService.create(request, List.of()))
        .isInstanceOf(ChannelNotFoundException.class);
  }

  @Test
  @DisplayName("메시지 생성 실패 - 작성자 없음")
  void createMessage_fail_authorNotFound() {
    UUID channelId = UUID.randomUUID();
    UUID authorId = UUID.randomUUID();
    MessageCreateRequest request = new MessageCreateRequest("testContent", channelId, authorId);
    Channel mockChannel = new Channel(ChannelType.PUBLIC, "testChannel", "testDescription");

    // 채널은 있고, 작성자가 없는 상황
    given(channelRepository.findById(channelId)).willReturn(Optional.of(mockChannel));
    given(userRepository.findById(authorId)).willReturn(Optional.empty());

    assertThatThrownBy(() -> messageService.create(request, List.of()))
        .isInstanceOf(UserNotFoundException.class);
  }

  @Test
  @DisplayName("메시지 수정 성공")
  void updateMessage_success() {
    UUID messageId = UUID.randomUUID();
    UUID channelId = UUID.randomUUID();
    UUID authorId = UUID.randomUUID();
    MessageUpdateRequest request = new MessageUpdateRequest("newContent");
    Channel mockChannel = new Channel(ChannelType.PUBLIC, "testChannel", "testDescription");
    User mockAuthor = new User("testUser", "test@test.com", "password123", null);
    Message mockMessage = new Message("testContent", mockChannel, mockAuthor, List.of());
    UserDto mockAuthorDto = new UserDto(authorId, "testUser", "test@test.com", null, false);
    MessageDto mockMessageDto = new MessageDto(
        UUID.randomUUID(),
        Instant.now(),
        null,
        "newTestContent",
        channelId,
        mockAuthorDto,
        List.of()
    );

    given(messageRepository.findById(messageId)).willReturn(Optional.of(mockMessage));
    given(messageMapper.toDto(any(Message.class))).willReturn(mockMessageDto);

    MessageDto result = messageService.update(messageId, request);

    assertThat(result.content()).isEqualTo("newTestContent");
    assertThat(result.channelId()).isEqualTo(channelId);
    assertThat(result.author().id()).isEqualTo(authorId);

    then(messageMapper).should().toDto(any(Message.class));
    then(messageRepository).should().findById(messageId);
  }

  @Test
  @DisplayName("메시지 수정 실패")
  void updateMessage_fail() {
    UUID messageId = UUID.randomUUID();
    MessageUpdateRequest request = new MessageUpdateRequest("newContent");

    given(messageRepository.findById(messageId)).willReturn(Optional.empty());

    assertThatThrownBy(() -> messageService.update(messageId, request))
        .isInstanceOf(NoSuchElementException.class);
  }

  @Test
  @DisplayName("메시지 삭제 성공")
  void deleteMessage_success() {
    UUID messageId = UUID.randomUUID();

    given(messageRepository.existsById(messageId)).willReturn(true);

    messageService.delete(messageId);

    then(messageRepository).should().deleteById(messageId);
  }

  @Test
  @DisplayName("메시지 삭제 실패")
  void deleteMessage_fail() {
    UUID messageId = UUID.randomUUID();

    given(messageRepository.existsById(messageId)).willReturn(false);

    assertThatThrownBy(() -> messageService.delete(messageId))
        .isInstanceOf(NoSuchElementException.class);

    then(messageRepository).should(never()).deleteById(messageId);
  }

  @Test
  @DisplayName("채널 ID로 메시지 목록 조회 성공")
  void findAllByChannelId_success() {
    // given
    UUID channelId = UUID.randomUUID();
    UUID authorId = UUID.randomUUID();
    Instant cursor = Instant.now();
    Pageable pageable = PageRequest.of(0, 50, Sort.by(Direction.DESC, "createdAt"));

    Channel mockChannel = new Channel(ChannelType.PUBLIC, "testChannel", "testDescription");
    User mockAuthor = new User("testUser", "test@test.com", "password", null);
    Message mockMessage = new Message("testContent", mockChannel, mockAuthor, List.of());

    UserDto mockAuthorDto = new UserDto(authorId, "testUser", "test@test.com", null, false);
    MessageDto mockMessageDto = new MessageDto(
        UUID.randomUUID(), Instant.now(), null,
        "testContent", channelId, mockAuthorDto, List.of()
    );

    // Slice는 페이징 결과를 담는 객체
    Slice<Message> mockSlice = new SliceImpl<>(List.of(mockMessage), pageable, false);
    PageResponse<MessageDto> mockPageResponse = new PageResponse<>(
        List.of(mockMessageDto), null, 1, false, 1L
    );

    given(messageRepository.findAllByChannelIdWithAuthor(any(), any(), any()))
        .willReturn(mockSlice);
    given(messageMapper.toDto(any(Message.class))).willReturn(mockMessageDto);
    given(pageResponseMapper.<MessageDto>fromSlice(any(), any())).willReturn(mockPageResponse);

    // when
    PageResponse<MessageDto> result = messageService.findAllByChannelId(channelId, cursor,
        pageable);

    // then
    assertThat(result.content()).hasSize(1);
    assertThat(result.content().get(0).content()).isEqualTo("testContent");
    then(messageRepository).should().findAllByChannelIdWithAuthor(any(), any(), any());
  }

  @Test
  @DisplayName("채널 ID로 메시지 목록 조회 - 메시지 없음")
  void findAllByChannelId_empty() {
    UUID channelId = UUID.randomUUID();
    Pageable pageable = PageRequest.of(0, 50, Sort.by(Direction.DESC, "createdAt"));

    Slice<Message> emptySlice = new SliceImpl<>(List.of(), pageable, false);
    PageResponse<MessageDto> emptyPageResponse = new PageResponse<>(
        List.of(), null, 0, false, 0L
    );

    given(messageRepository.findAllByChannelIdWithAuthor(any(), any(), any()))
        .willReturn(emptySlice);
    given(pageResponseMapper.<MessageDto>fromSlice(any(), any())).willReturn(emptyPageResponse);

    PageResponse<MessageDto> result = messageService.findAllByChannelId(channelId, null, pageable);

    // 예외 없이 빈 결과 반환
    assertThat(result.content()).isEmpty();
  }
}
