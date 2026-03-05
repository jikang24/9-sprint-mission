package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;

import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.UUID;

public interface MessageService {

  Message create(MessageCreateRequest messageCreateRequest, Channel channel, User author,
      List<BinaryContentCreateRequest> binaryContentCreateRequests);

  Message find(UUID messageId);

  List<Message> findAllByChannelId(UUID channelId);

  Message update(UUID messageId, MessageUpdateRequest request);

  void delete(UUID messageId);
}
