package com.sprint.mission.discodeit.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.exception.ErrorCode;
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

@SpringBootTest          // 전체 Spring 컨텍스트 로드
@AutoConfigureMockMvc    // MockMvc 자동 설정 (슬라이스 테스트와 달리 직접 추가해야 해요)
@ActiveProfiles("test")  // application-test.yaml 활성화
@Transactional           // 각 테스트 후 DB 롤백 → 테스트 간 독립성 보장
class UserIntegrationTest {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @Test
  @DisplayName("유저 생성 성공 - 실제 DB에 저장되는지 확인")
  void createUser_success() throws Exception {
    // given
    UserCreateRequest request = new UserCreateRequest("testUser", "test@test.com", "password123");
    String requestJson = objectMapper.writeValueAsString(request);

    // when & then - HTTP 요청 + 응답 검증
    mockMvc.perform(multipart("/api/users")
            .file(new MockMultipartFile(
                "userCreateRequest", "",
                MediaType.APPLICATION_JSON_VALUE,
                requestJson.getBytes()
            )))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.username").value("testUser"))
        .andExpect(jsonPath("$.email").value("test@test.com"));

    // 실제 DB에 저장됐는지 확인 (슬라이스 테스트와의 핵심 차이!)
    mockMvc.perform(get("/api/users"))
        .andExpect(jsonPath("$[?(@.email == 'test@test.com')]").exists());
  }

  @Test
  @DisplayName("유저 생성 실패 - 이메일 중복")
  void createUser_fail_duplicateEmail() throws Exception {
    // given - 먼저 유저 생성
    UserCreateRequest request = new UserCreateRequest("testUser", "test@test.com", "password123");
    String requestJson = objectMapper.writeValueAsString(request);

    mockMvc.perform(multipart("/api/users")
            .file(new MockMultipartFile(
                "userCreateRequest", "",
                MediaType.APPLICATION_JSON_VALUE,
                requestJson.getBytes()
            )))
        .andExpect(status().isCreated());

    // when - 같은 이메일로 다시 생성 시도
    mockMvc.perform(multipart("/api/users")
            .file(new MockMultipartFile(
                "userCreateRequest", "",
                MediaType.APPLICATION_JSON_VALUE,
                requestJson.getBytes()
            )))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value(ErrorCode.DUPLICATE_USER.name()));
  }

  @Test
  @DisplayName("유저 목록 조회 성공")
  void findAllUsers_success() throws Exception {
    // given - 유저 생성
    UserCreateRequest request = new UserCreateRequest("testUser", "test@test.com", "password123");
    String requestJson = objectMapper.writeValueAsString(request);

    mockMvc.perform(multipart("/api/users")
        .file(new MockMultipartFile(
            "userCreateRequest", "",
            MediaType.APPLICATION_JSON_VALUE,
            requestJson.getBytes()
        )));

    // when & then
    mockMvc.perform(get("/api/users"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$[0].username").value("testUser"));
  }

  @Test
  @DisplayName("유저 삭제 성공 - 실제 DB에서 삭제되는지 확인")
  void deleteUser_success() throws Exception {
    // given - 유저 생성 후 ID 추출
    UserCreateRequest request = new UserCreateRequest("testUser", "test@test.com", "password123");
    String requestJson = objectMapper.writeValueAsString(request);

    MvcResult result = mockMvc.perform(multipart("/api/users")
            .file(new MockMultipartFile(
                "userCreateRequest", "",
                MediaType.APPLICATION_JSON_VALUE,
                requestJson.getBytes()
            )))
        .andReturn();

    // 응답 JSON에서 userId 추출
    String responseBody = result.getResponse().getContentAsString();
    String userId = objectMapper.readTree(responseBody).get("id").asText();

    // when
    mockMvc.perform(delete("/api/users/{userId}", userId))
        .andExpect(status().isNoContent());

    // then - 목록 조회로 삭제 확인
    mockMvc.perform(get("/api/users"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[?(@.email == 'test@test.com')]").doesNotExist());
  }
}