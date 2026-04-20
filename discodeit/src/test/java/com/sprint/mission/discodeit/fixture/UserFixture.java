package com.sprint.mission.discodeit.fixture;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.LoginRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import java.util.UUID;

public class UserFixture {

  // 공통으로 사용할 기본값
  public static final UUID USER_ID = UUID.randomUUID();
  public static final String USERNAME = "testUser";
  public static final String EMAIL = "test@test.com";
  public static final String PASSWORD = "password123";

  // User 엔티티
  public static User createUser() {
    return new User(USERNAME, EMAIL, PASSWORD, null);
  }

  // UserDto
  public static UserDto createUserDto() {
    return new UserDto(USER_ID, USERNAME, EMAIL, null, false);
  }

  // Updated UserDto
  public static UserDto createUpdatedUserDto() {
    return new UserDto(USER_ID, "newUsername", "newEmail@test.com", null, false);
  }

  // UserCreateRequest
  public static UserCreateRequest createUserCreateRequest() {
    return new UserCreateRequest(USERNAME, EMAIL, PASSWORD);
  }

  // UserUpdateRequest
  public static UserUpdateRequest createUserUpdateRequest() {
    return new UserUpdateRequest("newUsername", "newEmail@test.com", "newPassword");
  }

  // LoginRequest
  public static LoginRequest createLoginRequest() {
    return new LoginRequest(USERNAME, PASSWORD);
  }
}