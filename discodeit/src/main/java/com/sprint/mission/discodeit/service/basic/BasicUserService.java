package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.detail.UserExceptionDetail;
import com.sprint.mission.discodeit.exception.user.UserAlreadyExistsException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.time.Instant;
import java.util.List;
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
      throw new UserAlreadyExistsException(UserExceptionDetail.ofEmail(email));
    }
    if (userRepository.existsByUsername(username)) {
      log.warn("User creation failed - username already exists: {}", username);
      throw new UserAlreadyExistsException(UserExceptionDetail.ofUsername(username));
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

    User user = new User(username, email, password, nullableProfile);
    Instant now = Instant.now();
    UserStatus userStatus = new UserStatus(user, now);

    userRepository.save(user);

    log.info("User created - username: {}, email: {}", username, email);
    if (nullableProfile != null) {
      log.debug("Profile uploaded - id: {}", nullableProfile.getId());
    }

    return userMapper.toDto(user);

  }

  @Override
  public UserDto find(UUID userId) {
    return userRepository.findById(userId)
        .map(userMapper::toDto)
        .orElseThrow(
            () -> new UserNotFoundException(UserExceptionDetail.ofUserId(userId.toString())));
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
          return new UserNotFoundException(UserExceptionDetail.ofUserId(userId.toString()));
        });

    String newUsername = userUpdateRequest.newUsername();
    String newEmail = userUpdateRequest.newEmail();
    if (userRepository.existsByEmail(newEmail)) {
      log.warn("User update failed - email already exists: {}", newEmail);
      throw new UserAlreadyExistsException(UserExceptionDetail.ofEmail(newEmail));
    }
    if (userRepository.existsByUsername(newUsername)) {
      log.warn("User update failed - username already exists: {}", newUsername);
      throw new UserAlreadyExistsException(UserExceptionDetail.ofUsername(newUsername));
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

    String newPassword = userUpdateRequest.newPassword();
    user.update(newUsername, newEmail, newPassword, nullableProfile);

    log.info("User updated - username: {}, email: {}", newUsername, newEmail);
    if (nullableProfile != null) {
      log.debug("Profile uploaded - id: {}", nullableProfile.getId());
    }
    return userMapper.toDto(user);
  }

  @Transactional
  @Override
  public void delete(UUID userId) {
    log.debug("Deleting user - userId: {}", userId);

    User user = userRepository.findById(userId)
        .orElseThrow(() -> {
          log.warn("User deletion failed - user not found: {}", userId);
          return new UserNotFoundException(UserExceptionDetail.ofUserId(userId.toString()));
        });
    userRepository.deleteById(userId);
    log.info("User deleted - username: {}", user.getUsername());
  }
}
