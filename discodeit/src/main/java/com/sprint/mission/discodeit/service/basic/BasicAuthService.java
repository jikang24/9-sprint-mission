package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.secure.DiscodeitUserDetails;
import com.sprint.mission.discodeit.service.AuthService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicAuthService implements AuthService {

  private final SessionRegistry sessionRegistry;

  public void closeUserSession(UUID userId) {
    log.info("유저 HtppSession 탐색");
    sessionRegistry.getAllPrincipals().stream()
        .filter(principal -> principal instanceof DiscodeitUserDetails)
        .map(principal -> (DiscodeitUserDetails) principal)
        .filter(userDetails -> userDetails.getUserDto().id().equals(userId))
        .forEach(userDetails -> {
          sessionRegistry.getAllSessions(userDetails, false)
              .forEach(SessionInformation::expireNow);
        });
    log.info("유저 세션 만료 처리 시작: userId={}", userId);
  }

}
