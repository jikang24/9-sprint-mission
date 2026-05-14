package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.AuthApi;
import com.sprint.mission.discodeit.secure.DiscodeitUserDetails;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.UserRoleUpdateRequest;
import com.sprint.mission.discodeit.entity.UserRole;
import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.UserService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController implements AuthApi {

  private final AuthService authService;
  private final UserService userService;


  // 클라이언트가 페이지 로드 시 이 API를 호출 → 서버가 쿠키로 CSRF 토큰 발급
  @Override
  @GetMapping("csrf-token")
  public ResponseEntity<Void> getCsrfToken(CsrfToken csrfToken) {
    // CsrfToken은 Spring이 자동으로 주입해줌 (HandlerMethodArgumentResolver)
    // GET 요청은 CSRF 검증을 안 하므로, 명시적으로 .getToken() 호출해서 토큰 초기화
    String tokenValue = csrfToken.getToken();
    log.debug("CSRF 토큰 요청: {}", tokenValue);
    return ResponseEntity.status(HttpStatus.NON_AUTHORITATIVE_INFORMATION).build();
  }

  @GetMapping("me")
  public ResponseEntity<UserDto> getMe(@AuthenticationPrincipal DiscodeitUserDetails userDetails) {
    log.info("내 정보 조회");
    UUID userId = userDetails.getUserDto().id();
    UserDto userDto = userService.find(userId);

    return ResponseEntity.status(HttpStatus.OK).body(userDto);
  }

  @PutMapping("role")
  public ResponseEntity<UserDto> updateRole(
      @RequestBody UserRoleUpdateRequest userRoleUpdateRequest) {
    log.info("유저 권한 업데이트");
    UUID userId = userRoleUpdateRequest.userId();
    UserRole role = userRoleUpdateRequest.role();

    UserDto userDto = userService.updateRole(userId, role);

    return ResponseEntity.status(HttpStatus.OK).body(userDto);
  }

}
