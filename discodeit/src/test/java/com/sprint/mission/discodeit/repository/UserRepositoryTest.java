package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import java.time.Instant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest                // JPA 관련 빈만 띄워요 (서비스, 컨트롤러 제외)
@ActiveProfiles("test")     // application-test.yaml 활성화
@EnableJpaAuditing          // createdAt 등 Audit 기능 활성화
class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private TestEntityManager em;  // DB에 테스트 데이터를 직접 저장할 때 사용

  @Test
  @DisplayName("이메일로 유저 존재 여부 확인 - 존재함")
  void existsByEmail_true() {
    // given - 테스트 데이터 저장
    User user = new User("testUser", "test@test.com", "password123", null);
    em.persist(user);
    em.flush();  // DB에 즉시 반영

    // when
    boolean result = userRepository.existsByEmail("test@test.com");

    // then
    assertThat(result).isTrue();
  }

  @Test
  @DisplayName("이메일로 유저 존재 여부 확인 - 존재하지 않음")
  void existsByEmail_false() {
    // given - 아무 데이터도 없는 상황

    // when
    boolean result = userRepository.existsByEmail("notexist@test.com");

    // then
    assertThat(result).isFalse();
  }

  @Test
  @DisplayName("유저명으로 유저 존재 여부 확인 - 존재함")
  void existsByUsername_true() {
    // given
    User user = new User("testUser", "test@test.com", "password123", null);
    em.persist(user);
    em.flush();

    // when
    boolean result = userRepository.existsByUsername("testUser");

    // then
    assertThat(result).isTrue();
  }

  @Test
  @DisplayName("유저명으로 유저 존재 여부 확인 - 존재하지 않음")
  void existsByUsername_false() {
    // when
    boolean result = userRepository.existsByUsername("notExistUser");

    // then
    assertThat(result).isFalse();
  }

  @Test
  @DisplayName("프로필과 상태 포함 전체 유저 조회: 성공 - 데이터 있음")
  void findAllWithProfileAndStatus_success() {
    // given
    User user = new User("testUser", "test@test.com", "password123", null);
    em.persist(user);
    UserStatus status = new UserStatus(user, Instant.now());
    em.persist(status);
    em.flush();

    // when
    var result = userRepository.findAllWithProfileAndStatus();

    // then
    assertThat(result).hasSize(1);
    assertThat(result.get(0).getUsername()).isEqualTo("testUser");
  }

  @Test
  @DisplayName("프로필과 상태 포함 전체 유저 조회: 실패 - 데이터 없음")
  void findAllWithProfileAndStatus_empty() {
    // when
    var result = userRepository.findAllWithProfileAndStatus();

    // then
    assertThat(result).isEmpty();
  }
}
