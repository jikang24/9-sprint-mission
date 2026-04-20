package com.sprint.mission.discodeit.fixture;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class MessageFixture {

  public static final UUID MESSAGE_ID = UUID.randomUUID();
  public static final String CONTENT = "testContent";

  // Message 엔티티
  public static Message createMessage() {
    return new Message(
        CONTENT,
        ChannelFixture.createPublicChannel(),
        UserFixture.createUser(),
        List.of()
    );
  }

  // MessageDto
  public static MessageDto createMessageDto() {
    return new MessageDto(
        MESSAGE_ID,
        Instant.now(),
        null,
        CONTENT,
        ChannelFixture.CHANNEL_ID,
        UserFixture.createUserDto(),
        List.of()
    );
  }

  // Updated MessageDto
  public static MessageDto createUpdatedMessageDto() {
    return new MessageDto(
        MESSAGE_ID,
        Instant.now(),
        null,
        "newTestContent",
        ChannelFixture.CHANNEL_ID,
        UserFixture.createUserDto(),
        List.of()
    );
  }

  // MessageCreateRequest
  public static MessageCreateRequest createMessageCreateRequest() {
    return new MessageCreateRequest(
        CONTENT,
        ChannelFixture.CHANNEL_ID,
        UserFixture.USER_ID
    );
  }

  // MessageUpdateRequest
  public static MessageUpdateRequest createMessageUpdateRequest() {
    return new MessageUpdateRequest("newContent");
  }
}