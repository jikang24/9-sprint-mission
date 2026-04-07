package com.sprint.mission.discodeit.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ChannelIntegrationTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @Autowired
  ChannelRepository channelRepository;

  // 채널 생성 후 ID를 추출하는 헬퍼 메소드 (반복 코드 제거)
  private String createPublicChannelAndGetId(String name, String description) throws Exception {
    PublicChannelCreateRequest request = new PublicChannelCreateRequest(name, description);
    String requestJson = objectMapper.writeValueAsString(request);

    MvcResult result = mockMvc.perform(post("/api/channels/public")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson))
        .andExpect(status().isCreated())
        .andReturn();

    return objectMapper.readTree(result.getResponse().getContentAsString())
        .get("id").asText();
  }

  @Test
  @DisplayName("Public 채널 생성 성공 - 실제 DB에 저장되는지 확인")
  void createPublicChannel_success() throws Exception {
    // given
    PublicChannelCreateRequest request = new PublicChannelCreateRequest("testChannel",
        "testDescription");
    String requestJson = objectMapper.writeValueAsString(request);

    // when & then
    mockMvc.perform(post("/api/channels/public")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name").value("testChannel"))
        .andExpect(jsonPath("$.type").value("PUBLIC"));

    // 실제 DB에 저장됐는지 확인
    assertThat(channelRepository.count()).isEqualTo(1);
  }

  @Test
  @DisplayName("Public 채널 생성 실패 - 유효성 검증 실패")
  void createPublicChannel_fail_validation() throws Exception {
    // given - 채널 이름이 빈 문자열
    PublicChannelCreateRequest invalidRequest = new PublicChannelCreateRequest("",
        "testDescription");
    String requestJson = objectMapper.writeValueAsString(invalidRequest);

    // when & then
    mockMvc.perform(post("/api/channels/public")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestJson))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value(ErrorCode.VALIDATION_ERROR.name()));
  }

  @Test
  @DisplayName("채널 수정 성공")
  void updateChannel_success() throws Exception {
    // given - 채널 먼저 생성
    String channelId = createPublicChannelAndGetId("testChannel", "testDescription");

    PublicChannelUpdateRequest updateRequest = new PublicChannelUpdateRequest("newName",
        "newDescription");
    String updateJson = objectMapper.writeValueAsString(updateRequest);

    // when & then
    mockMvc.perform(patch("/api/channels/{channelId}", channelId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(updateJson))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("newName"))
        .andExpect(jsonPath("$.description").value("newDescription"));
  }

  @Test
  @DisplayName("채널 수정 실패 - 채널 없음")
  void updateChannel_fail_notFound() throws Exception {
    // given - 존재하지 않는 채널 ID
    String notExistChannelId = java.util.UUID.randomUUID().toString();
    PublicChannelUpdateRequest updateRequest = new PublicChannelUpdateRequest("newName",
        "newDescription");
    String updateJson = objectMapper.writeValueAsString(updateRequest);

    // when & then
    mockMvc.perform(patch("/api/channels/{channelId}", notExistChannelId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(updateJson))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code").value(ErrorCode.CHANNEL_NOT_FOUND.name()));
  }

  @Test
  @DisplayName("채널 삭제 성공 - 실제 DB에서 삭제되는지 확인")
  void deleteChannel_success() throws Exception {
    // given - 채널 먼저 생성
    String channelId = createPublicChannelAndGetId("testChannel", "testDescription");

    // when
    mockMvc.perform(delete("/api/channels/{channelId}", channelId))
        .andExpect(status().isNoContent());

    // then - 실제 DB에서 삭제됐는지 확인
    assertThat(channelRepository.count()).isEqualTo(0);
  }
}