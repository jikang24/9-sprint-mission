package com.sprint.mission.discodeit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.LoginRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.fixture.UserFixture;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.basic.BasicAuthService;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

  @InjectMocks
  private BasicAuthService authService;

  @Mock
  private UserRepository userRepository;

  @Mock
  private UserMapper userMapper;

  @Test
  @DisplayName("유저 로그인 성공")
  void login_success() {
    User mockUser = UserFixture.createUser();
    UserDto mockUserDto = UserFixture.createUserDto();
    LoginRequest request = UserFixture.createLoginRequest();

    given(userRepository.findByUsername("testUser")).willReturn(Optional.of(mockUser));
    given(userMapper.toDto(mockUser)).willReturn(mockUserDto);

    UserDto result = authService.login(request);

    assertThat(result.username()).isEqualTo("testUser");
    then(userRepository).should().findByUsername("testUser");
    then(userMapper).should().toDto(mockUser);
  }

  @Test
  @DisplayName("로그인 실패 - 유저 없음")
  void login_failed_userNotFound() {
    LoginRequest request = UserFixture.createLoginRequest();

    given(userRepository.findByUsername("notExist")).willReturn(Optional.empty());

    assertThatThrownBy(() -> authService.login(request))
        .isInstanceOf(UserNotFoundException.class);
  }

  @Test
  @DisplayName("로그인 실패 - 비밀번호 불일치")
  void login_failed_wrongPassword() {
    LoginRequest request = UserFixture.createLoginRequest();
    User mockUser = UserFixture.createUser();

    given(userRepository.findByUsername("testUser")).willReturn(Optional.of(mockUser));

    assertThatThrownBy(() -> authService.login(request))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Wrong password");
    then(userMapper).shouldHaveNoInteractions();
  }

}
