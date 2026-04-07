package com.sprint.mission.discodeit.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.GlobalExceptionHandler;
import com.sprint.mission.discodeit.exception.detail.UserExceptionDetail;
import com.sprint.mission.discodeit.exception.user.UserAlreadyExistsException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
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

@WebMvcTest(UserController.class)
@Import(GlobalExceptionHandler.class)
class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;

  @MockitoBean
  private UserService userService;
  @MockitoBean
  private UserStatusService userStatusService;

  @Test
  @DisplayName("유저 생성 성공")
  void createUser_success() throws Exception {
    UUID userId = UUID.randomUUID();
    UserDto mockUserDto = new UserDto(userId, "testUser", "test@test.com", null, false);

    UserCreateRequest request = new UserCreateRequest("testUser", "test@test.com", "password123");
    String requestJson = objectMapper.writeValueAsString(request);

    given(userService.create(any(), any())).willReturn(mockUserDto);

    // when & then
    mockMvc.perform(multipart("/api/users")
            // userCreateRequest 파트 - JSON 형태로 전송
            .file(new MockMultipartFile(
                "userCreateRequest",
                "users.json",
                MediaType.APPLICATION_JSON_VALUE,
                requestJson.getBytes()
            )))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.code").value(ErrorCode.DUPLICATE_USER.name()))
        .andExpect(jsonPath("$.code").value(ErrorCode.USER_NOT_FOUND.name()));
  }

  @Test
  @DisplayName("유저 생성 실패 - 이메일 중복")
  void createUser_fail_duplicatedEmail() throws Exception {
    UserCreateRequest request = new UserCreateRequest("testUser", "test@test.com", "password123");
    String requestJson = objectMapper.writeValueAsString(request);

    given(userService.create(any(), any())).willThrow(
        new UserAlreadyExistsException(UserExceptionDetail.ofEmail("test@test.com")));

    mockMvc.perform(multipart("/api/users")
            .file(new MockMultipartFile(
                "userCreateRequest",
                "userCreateFail.json",
                MediaType.APPLICATION_JSON_VALUE,
                requestJson.getBytes()
            )))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.code").value("DUPLICATE_EMAIL"));
  }

  @Test
  @DisplayName("유저 삭제 성공")
  void deleteUser_success() throws Exception {
    UUID userId = UUID.randomUUID();
    willDoNothing().given(userService).delete(userId);

    mockMvc.perform(delete("/api/users/{userId}", userId))
        .andExpect(status().isNoContent());
  }

  @Test
  @DisplayName("유저 삭제 실패 - 유저 없음")
  void deleteUser_fail_notFount() throws Exception {
    UUID userId = UUID.randomUUID();

    willThrow(new UserNotFoundException(UserExceptionDetail.ofUserId(userId.toString())))
        .given(userService).delete(any());

    mockMvc.perform(delete("/api/users/{userId}", userId))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.code").value(ErrorCode.USER_NOT_FOUND.name()));
  }


}
