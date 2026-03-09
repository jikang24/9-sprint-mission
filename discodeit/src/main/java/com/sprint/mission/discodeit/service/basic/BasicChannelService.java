package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channelRepository;
  private final ReadStatusRepository readStatusRepository;
  private final MessageRepository messageRepository;
  private final UserRepository userRepository;
  private final ChannelMapper channelMapper;

  @Transactional
  @Override
  public ChannelDto create(PublicChannelCreateRequest request) {
    String name = request.name();
    String description = request.description();

    Channel channel = new Channel(ChannelType.PUBLIC, name, description);
    Channel savedChannel = channelRepository.save(channel);

    return toChannelDto(savedChannel);
  }

  @Transactional
  @Override
  public ChannelDto create(PrivateChannelCreateRequest request) {
    Channel channel = new Channel(ChannelType.PRIVATE, null, null);
    Channel createdChannel = channelRepository.save(channel);

    Set<UUID> participantIds = new HashSet<>(request.participantIds());
    List<User> participants = userRepository.findAllById(participantIds);

    if (participants.size() != participantIds.size()) {
      Set<UUID> foundIds = participants.stream()
          .map(User::getId)
          .collect(Collectors.toSet());

      Set<UUID> missingIds = new HashSet<>(participantIds);
      missingIds.removeAll(foundIds);

      throw new NoSuchElementException("Users not found: " + missingIds);
    }

    Instant now = Instant.now();

    participants.forEach(participant ->
        readStatusRepository.save(
            new ReadStatus(participant, createdChannel, now)
        )
    );

    return toChannelDto(createdChannel);
  }


  @Override
  public List<ChannelDto> findAllByUserId(UUID userId) {
    List<UUID> mySubscribedChannelIds = readStatusRepository.findAllByUserId(userId).stream()
        .map(readStatus -> readStatus.getChannel().getId())
        .toList();

    return channelRepository.findAll().stream()
        .filter(channel ->
            channel.getType().equals(ChannelType.PUBLIC)
                || mySubscribedChannelIds.contains(channel.getId())
        )
        .map(this::toChannelDto)
        .toList();
  }

  @Transactional
  @Override
  public ChannelDto update(UUID channelId, PublicChannelUpdateRequest request) {
    String newName = request.newName();
    String newDescription = request.newDescription();
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(
            () -> new NoSuchElementException("Channel with id " + channelId + " not found"));
    if (channel.getType().equals(ChannelType.PRIVATE)) {
      throw new IllegalArgumentException("Private channel cannot be updated");
    }
    channel.update(newName, newDescription);
    Channel savedChannel = channelRepository.save(channel);

    return toChannelDto(savedChannel);
  }

  @Transactional
  @Override
  public void delete(UUID channelId) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(
            () -> new NoSuchElementException("Channel with id " + channelId + " not found"));

    messageRepository.deleteAllByChannelId(channel.getId());
    readStatusRepository.deleteAllByChannelId(channel.getId());
    channelRepository.deleteById(channelId);
  }

  private ChannelDto toChannelDto(Channel channel) {
    Instant lastMessageAt = messageRepository.findAllByChannelId(channel.getId()).stream()
        .map(Message::getCreatedAt)
        .max(Instant::compareTo)
        .orElse(null);

    List<UUID> participantIds = new ArrayList<>();
    if (channel.getType() == ChannelType.PRIVATE) {
      participantIds = readStatusRepository.findAllByChannelId(channel.getId()).stream()
          .map(readStatus -> readStatus.getUser().getId())
          .distinct()
          .toList();
    }

    return channelMapper.toDto(channel, participantIds, lastMessageAt);
  }
}
