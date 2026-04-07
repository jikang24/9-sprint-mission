package com.sprint.mission.discodeit.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.MessageRepository;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class MessageIntegrationTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @Autowired
  MessageRepository messageRepository;

  // 유저 생성 후 ID 반환
  private String createUserAndGetId(String username, String email) throws Exception {
    UserCreateRequest request = new UserCreateRequest(username, email, "password123");
    String requestJson = objectMapper.writeValueAsString(request);

    MvcResult result = mockMvc.perform(multipart("/api/users")
            .file(new MockMultipartFile(
                "userCreateRequest", "",
                MediaType.APPLICATION_JSON_VALUE,
                requestJson.getBytes()
            )))
        .andExpect(status().isCreated())
        .andReturn();

    return objectMapper.readTree(result.getResponse().getContentAsString())
        .get("id").asText();
  }

  // 채널 생성 후 ID 반환
  private String createChannelAndGetId(String name) throws Exception {
    PublicChannelCreateRequest request = new PublicChannelCreateRequest(name, "description");
    String requestJson = objectMapper.writeValueAsString(request);

    MvcResult result = mockMvc.perform(post("/api/channels/public")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson))
        .andExpect(status().isCreated())
        .andReturn();

    return objectMapper.readTree(result.getResponse().getContentAsString())
        .get("id").asText();
  }

  // 메시지 생성 후 ID 반환
  private String createMessageAndGetId(String content, String channelId, String authorId)
      throws Exception {
    MessageCreateRequest request = new MessageCreateRequest(
        content,
        UUID.fromString(channelId),
        UUID.fromString(authorId)
    );
    String requestJson = objectMapper.writeValueAsString(request);

    MvcResult result = mockMvc.perform(multipart("/api/messages")
            .file(new MockMultipartFile(
                "messageCreateRequest", "",
                MediaType.APPLICATION_JSON_VALUE,
                requestJson.getBytes()
            )))
        .andExpect(status().isCreated())
        .andReturn();

    return objectMapper.readTree(result.getResponse().getContentAsString())
        .get("id").asText();
  }

  @Test
  @DisplayName("메시지 생성 성공 - 실제 DB에 저장되는지 확인")
  void createMessage_success() throws Exception {
    // given - 유저와 채널 먼저 생성
    String authorId = createUserAndGetId("testUser", "test@test.com");
    String channelId = createChannelAndGetId("testChannel");

    MessageCreateRequest request = new MessageCreateRequest(
        "testContent",
        UUID.fromString(channelId),
        UUID.fromString(authorId)
    );
    String requestJson = objectMapper.writeValueAsString(request);

    // when & then
    mockMvc.perform(multipart("/api/messages")
            .file(new MockMultipartFile(
                "messageCreateRequest", "",
                MediaType.APPLICATION_JSON_VALUE,
                requestJson.getBytes()
            )))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.content").value("testContent"));

    // 실제 DB에 저장됐는지 확인
    assertThat(messageRepository.count()).isEqualTo(1);
  }

  @Test
  @DisplayName("메시지 생성 실패 - 채널 없음")
  void createMessage_fail_channelNotFound() throws Exception {
    // given - 유저만 생성, 채널은 존재하지 않는 ID 사용
    String authorId = createUserAndGetId("testUser", "test@test.com");
    String notExistChannelId = UUID.randomUUID().toString();

    MessageCreateRequest request = new MessageCreateRequest(
        "testContent",
        UUID.fromString(notExistChannelId),
        UUID.fromString(authorId)
    );
    String requestJson = objectMapper.writeValueAsString(request);

    // when & then
    mockMvc.perform(multipart("/api/messages")
            .file(new MockMultipartFile(
                "messageCreateRequest", "",
                MediaType.APPLICATION_JSON_VALUE,
                requestJson.getBytes()
            )))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code").value(ErrorCode.CHANNEL_NOT_FOUND.name()));
  }

  @Test
  @DisplayName("메시지 수정 성공")
  void updateMessage_success() throws Exception {
    // given - 유저, 채널, 메시지 순서로 생성
    String authorId = createUserAndGetId("testUser", "test@test.com");
    String channelId = createChannelAndGetId("testChannel");
    String messageId = createMessageAndGetId("testContent", channelId, authorId);

    MessageUpdateRequest updateRequest = new MessageUpdateRequest("newContent");
    String updateJson = objectMapper.writeValueAsString(updateRequest);

    // when & then
    mockMvc.perform(patch("/api/messages/{messageId}", messageId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(updateJson))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").value("newContent"));
  }

  @Test
  @DisplayName("메시지 삭제 성공 - 실제 DB에서 삭제되는지 확인")
  void deleteMessage_success() throws Exception {
    // given - 유저, 채널, 메시지 순서로 생성
    String authorId = createUserAndGetId("testUser", "test@test.com");
    String channelId = createChannelAndGetId("testChannel");
    String messageId = createMessageAndGetId("testContent", channelId, authorId);

    // when
    mockMvc.perform(delete("/api/messages/{messageId}", messageId))
        .andExpect(status().isNoContent());

    // then - 실제 DB에서 삭제됐는지 확인
    assertThat(messageRepository.count()).isZero();
  }

  @Test
  @DisplayName("채널별 메시지 목록 조회 성공")
  void findAllByChannelId_success() throws Exception {
    // given - 유저, 채널, 메시지 생성
    String authorId = createUserAndGetId("testUser", "test@test.com");
    String channelId = createChannelAndGetId("testChannel");
    createMessageAndGetId("testContent", channelId, authorId);

    // when & then
    mockMvc.perform(get("/api/messages")
            .param("channelId", channelId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray())
        .andExpect(jsonPath("$.content[0].content").value("testContent"));
  }
}
