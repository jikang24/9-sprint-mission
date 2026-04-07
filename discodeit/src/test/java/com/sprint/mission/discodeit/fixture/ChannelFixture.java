package com.sprint.mission.discodeit.fixture;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import java.util.List;
import java.util.UUID;

public class ChannelFixture {

  public static final UUID CHANNEL_ID = UUID.randomUUID();
  public static final String CHANNEL_NAME = "testChannel";
  public static final String CHANNEL_DESCRIPTION = "testDescription";

  // Public Channel 엔티티
  public static Channel createPublicChannel() {
    return new Channel(ChannelType.PUBLIC, CHANNEL_NAME, CHANNEL_DESCRIPTION);
  }

  // Private Channel 엔티티
  public static Channel createPrivateChannel() {
    return new Channel(ChannelType.PRIVATE, null, null);
  }

  // ChannelDto
  public static ChannelDto createPublicChannelDto() {
    return new ChannelDto(CHANNEL_ID, ChannelType.PUBLIC, CHANNEL_NAME,
        CHANNEL_DESCRIPTION, null, null);
  }

  // PublicChannelCreateRequest
  public static PublicChannelCreateRequest createPublicChannelCreateRequest() {
    return new PublicChannelCreateRequest(CHANNEL_NAME, CHANNEL_DESCRIPTION);
  }

  // PublicChannelUpdateRequest
  public static PublicChannelUpdateRequest createPublicChannelUpdateRequest() {
    return new PublicChannelUpdateRequest("newName", "newDescription");
  }

  // PrivateChannelCreateRequest
  public static PrivateChannelCreateRequest createPrivateChannelCreateRequest() {
    return new PrivateChannelCreateRequest(
        List.of(UUID.randomUUID(), UUID.randomUUID())
    );
  }
}