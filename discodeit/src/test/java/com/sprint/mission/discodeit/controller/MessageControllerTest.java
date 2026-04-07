package com.sprint.mission.discodeit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.GlobalExceptionHandler;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.detail.ChannelExceptionDetail;
import com.sprint.mission.discodeit.exception.detail.MessageExceptionDetail;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MessageController.class)
@Import(GlobalExceptionHandler.class)
class MessageControllerTest {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private MessageService messageService;
  @MockitoBean
  private ReadStatusService readStatusService;

  @Test
  @DisplayName("메시지 생성 성공")
  void createMessage_success() throws Exception {
    UUID channelId = UUID.randomUUID();
    UUID authorId = UUID.randomUUID();

    MessageCreateRequest request = new MessageCreateRequest("testContent", channelId, authorId);
    String requestJson = objectMapper.writeValueAsString(request);

    UserDto mockAuthorDto = new UserDto(authorId, "testUser", "test@test.com", null, false);
    MessageDto mockMessageDto = new MessageDto(
        UUID.randomUUID(), Instant.now(), null,
        "testContent", channelId, mockAuthorDto, List.of()
    );
    given(messageService.create(any(), any())).willReturn(mockMessageDto);

    mockMvc.perform(multipart("/api/messages")
            .file(new MockMultipartFile(
                "messageCreateRequest",
                "messages.json",
                MediaType.APPLICATION_JSON_VALUE,
                requestJson.getBytes()
            )))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.content").value("testContent"));
  }

  @Test
  @DisplayName("메시지 생성 실패 - 채널 없음")
  void createMessage_fail_channelNotFound() throws Exception {
    UUID channelId = UUID.randomUUID();
    UUID authorId = UUID.randomUUID();

    MessageCreateRequest request = new MessageCreateRequest("testContent", channelId, authorId);
    String requestJson = objectMapper.writeValueAsString(request);

    given(messageService.create(any(), any())).willThrow(new ChannelNotFoundException(
        ChannelExceptionDetail.ofChannelId(channelId.toString())));

    mockMvc.perform(multipart("/api/messages")
            .file(new MockMultipartFile(
                "messageCreateRequest",
                "messages.json",
                MediaType.APPLICATION_JSON_VALUE,
                requestJson.getBytes()
            )))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code").value(ErrorCode.CHANNEL_NOT_FOUND.name()));
  }

  @Test
  @DisplayName("메시지 수정 성공")
  void updateMessage_success() throws Exception {
    UUID messageId = UUID.randomUUID();
    UUID channelId = UUID.randomUUID();

    MessageDto mockMessageDto = new MessageDto(messageId, null, null, "newContent", channelId,
        null, null);

    MessageUpdateRequest request = new MessageUpdateRequest("newContent");
    String requestJson = objectMapper.writeValueAsString(request);

    given(messageService.update(any(UUID.class), any(MessageUpdateRequest.class))).willReturn(
        mockMessageDto);

    mockMvc.perform(patch("/api/messages/{messageId}", messageId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").value("newContent"));
  }

  @Test
  @DisplayName("메시지 수정 실패")
  void updateMessage_fail() throws Exception {
    UUID messageId = UUID.randomUUID();
    MessageUpdateRequest request = new MessageUpdateRequest("newContent");
    String requestJson = objectMapper.writeValueAsString(request);

    given(messageService.update(any(UUID.class), any(MessageUpdateRequest.class))).willThrow(
        new MessageNotFoundException(MessageExceptionDetail.ofMessageId(messageId.toString())));
    mockMvc.perform(patch("/api/messages/{messageId}", messageId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code").value(ErrorCode.MESSAGE_NOT_FOUND.name()));
  }

}
