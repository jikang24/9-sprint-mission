package com.sprint.mission.discodeit.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.exception.GlobalExceptionHandler;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.PrivateChannelUpdateException;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ChannelController.class)
@Import(GlobalExceptionHandler.class)
class ChannelControllerTest {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private ChannelService channelService;

  @Test
  @DisplayName("채널 생성 성공")
  void createChannel_success() throws Exception {
    UUID channelId = UUID.randomUUID();
    ChannelDto mockChannelDto = new ChannelDto(channelId, ChannelType.PUBLIC, "testChannel",
        "testDescription", null, null);

    PublicChannelCreateRequest request = new PublicChannelCreateRequest("testChannel",
        "testDescription");
    String requestJson = objectMapper.writeValueAsString(request);

    given(channelService.create(any(PublicChannelCreateRequest.class))).willReturn(mockChannelDto);

    mockMvc.perform(post("/api/channels/public")  // URL도 수정
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name").value("testChannel"));
  }

  @Test
  @DisplayName("Public 채널 생성 실패 - 유효성 검증 실패")
  void createChannel_fail_validation() throws Exception {
    PublicChannelCreateRequest invalidRequest = new PublicChannelCreateRequest("thisChannelNameIsTooLong!",
        "testDescription");
    String requestJson = objectMapper.writeValueAsString(invalidRequest);

    mockMvc.perform(post("/api/channels/public")  // URL도 수정
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"));
  }

  @Test
  @DisplayName("Private 채널 수정 시도 실패")
  void updatePrivateAttempt_fail() throws Exception {
    UUID channelId = UUID.randomUUID();
    PublicChannelUpdateRequest request = new PublicChannelUpdateRequest("newName",
        "newDescription");

    String requestJson = objectMapper.writeValueAsString(request);

    given(channelService.update(any(UUID.class), any(PublicChannelUpdateRequest.class))).willThrow(
        new PrivateChannelUpdateException(Map.of("channelId", channelId)));

    mockMvc.perform(patch("/api/channels/{channelId}", channelId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson))
        .andExpect(status().isBadRequest())   // 400 검증
        .andExpect(jsonPath("$.code").value("PRIVATE_CHANNEL_UPDATE"));
  }

  @Test
  @DisplayName("채널 삭제 성공")
  void deleteChannel_success() throws Exception {
    UUID channelId = UUID.randomUUID();
    willDoNothing().given(channelService).delete(channelId);

    mockMvc.perform(delete("/api/channels/{channelId}", channelId))
        .andExpect(status().isNoContent());
  }

  @Test
  @DisplayName("채널 삭제 실패 - 채널 없음")
  void deleteChannel_fail_notFound() throws Exception {
    UUID channelId = UUID.randomUUID();

    willThrow(new ChannelNotFoundException(Map.of("channelId", channelId)))
        .given(channelService).delete(channelId);

    mockMvc.perform(delete("/api/channels/{channelId}", channelId))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code").value("CHANNEL_NOT_FOUND"));
  }


}
