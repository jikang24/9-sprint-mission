package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentStorage binaryContentStorage;
  private final MessageMapper messageMapper;

  @Transactional
  @Override
  public MessageDto create(MessageCreateRequest messageCreateRequest,
      Optional<List<BinaryContentCreateRequest>> optionalBinaryContentCreateRequests) {
    UUID channelId = messageCreateRequest.channelId();
    UUID authorId = messageCreateRequest.authorId();

    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new NoSuchElementException(
            "Channel with id " + channelId + " does not exist"));

    User author = userRepository.findById(authorId)
        .orElseThrow(() -> new NoSuchElementException(
            "Author with id " + authorId + " does not exist"));

    List<BinaryContent> attachments = optionalBinaryContentCreateRequests
        .orElseGet(List::of)
        .stream()
        .map(attachmentRequest -> {
          byte[] bytes = attachmentRequest.bytes();
          String fileName = attachmentRequest.fileName();
          String contentType = attachmentRequest.contentType();

          BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length,
              contentType);
          BinaryContent saved = binaryContentRepository.save(binaryContent);
          binaryContentStorage.put(saved.getId(), bytes);

          return saved;
        })
        .toList();

    String content = messageCreateRequest.content();
    Message message = new Message(
        content,
        channel,
        author,
        attachments
    );
    Message savedMessage = messageRepository.save(message);
    return messageMapper.toDto(savedMessage);
  }

  @Override
  public MessageDto find(UUID messageId) {
    Message message = messageRepository.findById(messageId)
        .orElseThrow(
            () -> new NoSuchElementException("Message with id " + messageId + " not found"));
    return messageMapper.toDto(message);
  }


  @Override
  public List<MessageDto> findAllByChannelId(UUID channelId) {
    return messageRepository.findAllByChannelId(channelId).stream()
        .map(messageMapper::toDto)
        .toList();
  }

  @Transactional
  @Override
  public MessageDto update(UUID messageId, MessageUpdateRequest request) {
    String newContent = request.newContent();

    Message message = messageRepository.findById(messageId)
        .orElseThrow(
            () -> new NoSuchElementException("Message with id " + messageId + " not found"));

    message.update(newContent);

    Message savedMessage = messageRepository.save(message);
    return messageMapper.toDto(savedMessage);
  }


  @Transactional
  @Override
  public void delete(UUID messageId) {
//    Message message = messageRepository.findById(messageId)
//        .orElseThrow(
//            () -> new NoSuchElementException("Message with id " + messageId + " not found"));
    if (!messageRepository.existsById(messageId)) {
      throw new NoSuchElementException("Message with id " + messageId + " not found");
    }
    messageRepository.deleteById(messageId);
  }
}
