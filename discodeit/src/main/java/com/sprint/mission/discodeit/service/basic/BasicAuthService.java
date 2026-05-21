package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.response.JwtDto;
import com.sprint.mission.discodeit.entity.JwtSession;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.JwtSessionRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.secure.DiscodeitUserDetails;
import com.sprint.mission.discodeit.secure.DiscodeitUserDetailsService;
import com.sprint.mission.discodeit.secure.JwtTokenProvider;
import com.sprint.mission.discodeit.service.AuthService;
import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicAuthService implements AuthService {

  private final JwtSessionRepository jwtSessionRepository;
  private final JwtTokenProvider jwtTokenProvider;
  private final DiscodeitUserDetailsService userDetailsService;
  private final UserRepository userRepository;

  @Override
  public JwtSession saveSession(UUID userId, String accessToken, String refreshToken) {
    Instant expiresAt = Instant.now()
        .plusSeconds(jwtTokenProvider.getRefreshTokenValiditySeconds());

    JwtSession jwtSession = new JwtSession(userId, accessToken, refreshToken, expiresAt);
    log.info("JWT 세션 저장 - userId: {}", userId);
    return jwtSessionRepository.save(jwtSession);
  }

  @Override
  public JwtDto refresh(String refreshToken) {
    JwtSession session = jwtSessionRepository.findByRefreshToken(refreshToken)
        .orElseThrow(() -> new IllegalArgumentException("Invalid refresh token"));

    if (session.getExpirationTime().isBefore(Instant.now())) {
      jwtSessionRepository.delete(session);
      throw new IllegalArgumentException("Refresh token has expired");
    }

    User user = userRepository.findById(session.getUserId())
        .orElseThrow(() -> new IllegalArgumentException("User not found"));

    DiscodeitUserDetails userDetails =
        (DiscodeitUserDetails) userDetailsService.loadUserByUsername(user.getUsername());

    String newAccessToken = jwtTokenProvider.generateAccessToken(userDetails);
    String newRefreshToken = jwtTokenProvider.generateRefreshToken();

    jwtSessionRepository.delete(session);
    saveSession(session.getUserId(), newAccessToken, newRefreshToken);

    log.info("Refresh token refreshed - userId: {}", session.getUserId());
    return JwtDto.builder()
        .userDto(userDetails.getUserDto())
        .accessToken(newAccessToken)
        .refreshToken(newRefreshToken)
        .build();
  }

  @Override
  @Transactional
  public void deleteSession(UUID userId) {
    jwtSessionRepository.deleteByUserId(userId);
    log.info("JWT 세션 삭제 - userId: {}", userId);
  }
}
