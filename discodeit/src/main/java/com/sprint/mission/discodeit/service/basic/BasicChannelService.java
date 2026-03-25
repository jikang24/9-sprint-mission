package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
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
    log.debug("Creating public channel - Channel name: {}", request.name());

    String name = request.name();
    String description = request.description();

    Channel channel = new Channel(ChannelType.PUBLIC, name, description);
    channelRepository.save(channel);

    log.info("Public channel created - name: {}, id: {}, description: {}",
        name, channel.getId(), description);

    return channelMapper.toDto(channel);
  }

  @Transactional
  @Override
  public ChannelDto create(PrivateChannelCreateRequest request) {
    log.debug("Creating private channel -  Participants: {}", request.participantIds());

    List<User> foundUsers = userRepository.findAllById(request.participantIds());
    if (foundUsers.size() != request.participantIds().size()) {
      log.warn("Some participants not found - requested: {}, found: {}",
          request.participantIds().size(), foundUsers.size());
    }

    Channel channel = new Channel(ChannelType.PRIVATE, null, null);
    channelRepository.save(channel);
    log.info("Private channel created - Channel type: {}, Channel id: {}",
        channel.getType(), channel.getId());

    List<ReadStatus> readStatuses = foundUsers.stream()
        .map(user -> new ReadStatus(user, channel, channel.getCreatedAt()))
        .toList();
    readStatusRepository.saveAll(readStatuses);

    log.debug("ReadStatus created - count: {}", readStatuses.size());

    return channelMapper.toDto(channel);
  }

  @Transactional(readOnly = true)
  @Override
  public ChannelDto find(UUID channelId) {
    return channelRepository.findById(channelId)
        .map(channelMapper::toDto)
        .orElseThrow(
            () -> new NoSuchElementException("Channel with id " + channelId + " not found"));
  }

  @Transactional(readOnly = true)
  @Override
  public List<ChannelDto> findAllByUserId(UUID userId) {
    List<UUID> mySubscribedChannelIds = readStatusRepository.findAllByUserId(userId).stream()
        .map(ReadStatus::getChannel)
        .map(Channel::getId)
        .toList();

    return channelRepository.findAllByTypeOrIdIn(ChannelType.PUBLIC, mySubscribedChannelIds)
        .stream()
        .map(channelMapper::toDto)
        .toList();
  }

  @Transactional
  @Override
  public ChannelDto update(UUID channelId, PublicChannelUpdateRequest request) {
    log.debug("Updating public channel - Channel id: {}", channelId);

    String newName = request.newName();
    String newDescription = request.newDescription();

    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> {
          log.warn("Channel update failed - channel not found: {}", channelId);
          return new NoSuchElementException("Channel with id " + channelId + " not found");
        });
    if (channel.getType().equals(ChannelType.PRIVATE)) {
      log.warn("Channel update failed - private channel cannot be updated: {}", channelId);
      throw new IllegalArgumentException("Private channel cannot be updated");
    }

    channel.update(newName, newDescription);
    log.info("Public channel updated - name: {}, id: {}, description: {}",
        newName, channelId, newDescription);

    return channelMapper.toDto(channel);
  }

  @Transactional
  @Override
  public void delete(UUID channelId) {
    log.debug("Deleting channel - Channel id: {}", channelId);
    if (!channelRepository.existsById(channelId)) {
      log.warn("Channel deletion failed - channel not found: {}", channelId);
      throw new NoSuchElementException("Channel with id " + channelId + " not found");
    }

    messageRepository.deleteAllByChannelId(channelId);
    readStatusRepository.deleteAllByChannelId(channelId);
    log.info("Messages & ReadStatus deleted - Channel id: {}", channelId);

    channelRepository.deleteById(channelId);
    log.info("Channel deleted - Channel id: {}", channelId);
  }
}
