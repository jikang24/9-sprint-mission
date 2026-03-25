package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final UserStatusRepository userStatusRepository;
  private final UserMapper userMapper;
  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentStorage binaryContentStorage;

  @Transactional
  @Override
  public UserDto create(UserCreateRequest userCreateRequest,
      Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
    String username = userCreateRequest.username();
    String email = userCreateRequest.email();

    log.debug("Creating user - username: {}, email: {}", username, email);

    if (userRepository.existsByEmail(email)) {
      log.warn("User creation failed - email already exists: {}", email);
      throw new IllegalArgumentException("User with email " + email + " already exists");
    }
    if (userRepository.existsByUsername(username)) {
      log.warn("User creation failed - username already exists: {}", username);
      throw new IllegalArgumentException("User with username " + username + " already exists");
    }

    BinaryContent nullableProfile = optionalProfileCreateRequest
        .map(profileRequest -> {
          String fileName = profileRequest.fileName();
          String contentType = profileRequest.contentType();
          byte[] bytes = profileRequest.bytes();
          BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length,
              contentType);
          binaryContentRepository.save(binaryContent);
          binaryContentStorage.put(binaryContent.getId(), bytes);
          return binaryContent;
        })
        .orElse(null);
    String password = userCreateRequest.password();

    try {
      User user = new User(username, email, password, nullableProfile);
      Instant now = Instant.now();
      UserStatus userStatus = new UserStatus(user, now);

      userRepository.save(user);

      log.info("User created - username: {}, email: {}", username, email);
      if (nullableProfile != null) {
        log.debug("Profile uploaded - id: {}", nullableProfile.getId());
      }

      return userMapper.toDto(user);
    } catch (Exception e) {
      log.error("Error creating user: {}", e.getMessage());
      throw e;
    }
  }

  @Override
  public UserDto find(UUID userId) {
    return userRepository.findById(userId)
        .map(userMapper::toDto)
        .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));
  }

  @Override
  public List<UserDto> findAll() {
    return userRepository.findAllWithProfileAndStatus()
        .stream()
        .map(userMapper::toDto)
        .toList();
  }

  @Transactional
  @Override
  public UserDto update(UUID userId, UserUpdateRequest userUpdateRequest,
      Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {

    log.debug("Updating user - userId: {}", userId);

    User user = userRepository.findById(userId)
        .orElseThrow(() -> {
          log.warn("User update failed - user not found: {}", userId);
          return new NoSuchElementException("User with id " + userId + " not found");
        });

    String newUsername = userUpdateRequest.newUsername();
    String newEmail = userUpdateRequest.newEmail();
    if (userRepository.existsByEmail(newEmail)) {
      log.warn("User update failed - email already exists: {}", newEmail);
      throw new IllegalArgumentException("User with email " + newEmail + " already exists");
    }
    if (userRepository.existsByUsername(newUsername)) {
      log.warn("User update failed - username already exists: {}", newUsername);
      throw new IllegalArgumentException("User with username " + newUsername + " already exists");
    }

    BinaryContent nullableProfile = optionalProfileCreateRequest
        .map(profileRequest -> {

          String fileName = profileRequest.fileName();
          String contentType = profileRequest.contentType();
          byte[] bytes = profileRequest.bytes();
          BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length,
              contentType);
          binaryContentRepository.save(binaryContent);
          binaryContentStorage.put(binaryContent.getId(), bytes);
          return binaryContent;
        })
        .orElse(null);
    try {

      String newPassword = userUpdateRequest.newPassword();
      user.update(newUsername, newEmail, newPassword, nullableProfile);

      log.info("User updated - username: {}, email: {}", newUsername, newEmail);
      if (nullableProfile != null) {
        log.debug("Profile uploaded - id: {}", nullableProfile.getId());
      }
      return userMapper.toDto(user);
    } catch (Exception e) {
      log.error("Error updating user: {}", e.getMessage());
      throw e;
    }
  }

  @Transactional
  @Override
  public void delete(UUID userId) {
    log.debug("Deleting user - userId: {}", userId);

    if (!userRepository.existsById(userId)) {
      log.warn("User deletion failed - user not found: {}", userId);
      throw new NoSuchElementException("User with id " + userId + " not found");
    }
    try {
      User user = userRepository.findById(userId)
          .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));
      log.info("User deleted - username: {}", user.getUsername());
      userRepository.deleteById(userId);
    } catch (Exception e) {
      log.error("Error deleting user: {}", e.getMessage());
      throw e;
    }
  }
}
