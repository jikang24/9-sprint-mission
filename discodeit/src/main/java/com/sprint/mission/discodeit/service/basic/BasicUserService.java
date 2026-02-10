package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.BinaryContentDTO;
import com.sprint.mission.discodeit.dto.UserDTO;
import com.sprint.mission.discodeit.dto.UserServiceResponseDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.status.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final UserStatusRepository userStatusRepository;

//    @Override
//    public User createUser(UserDTO.CreateUserDTO userCreateRequest) {
//        if(userRepository.existsByEmail(userCreateRequest.email())){
//            throw new IllegalArgumentException("이미 존재하는 email 입니다: " + userCreateRequest.email());
//        }
//        if(userRepository.existsByUserName(userCreateRequest.userName())){
//            throw new IllegalArgumentException("이미 존재하는 username 입니다: " + userCreateRequest.userName());
//        }
//
//        User user = new User(
//                userCreateRequest.userName(),
//                userCreateRequest.email(),
//                userCreateRequest.password()
//        );
//
//        UserStatus userStatus = new UserStatus(user.getUserId(), now);
//        userStatusRepository.save(userStatus);
//        userRepository.save(user);
//        return user;
//    }

    @Override
    public User createUser(UserDTO.CreateUserDTO userCreateRequest, Optional<BinaryContentDTO.CreateBinaryContentDTO> optionalProfileCreateRequest) {
        String username = userCreateRequest.userName();
        String email = userCreateRequest.email();

        if(userRepository.existsByEmail(userCreateRequest.email())){
            throw new IllegalArgumentException("이미 존재하는 email 입니다: " + userCreateRequest.email());
        }
        if(userRepository.existsByUserName(userCreateRequest.userName())){
            throw new IllegalArgumentException("이미 존재하는 username 입니다: " + userCreateRequest.userName());
        }

        UUID nullableProfileId = optionalProfileCreateRequest
                .map(profileRequest -> {
                    String profileFileName = profileRequest.fileName();
                    String profileContentType = profileRequest.contentType();
                    byte[] profileContent = profileRequest.content();
                    BinaryContent binaryContent = new BinaryContent(profileContent, profileContentType, profileFileName);
                    return binaryContentRepository.save(binaryContent).getId();
                })
                .orElse(null);
        String password = userCreateRequest.password();

        User user = new User(username, email, password, nullableProfileId);
        User createdUser = userRepository.save(user);

        Instant now = Instant.now();
        UserStatus userStatus = new UserStatus(createdUser.getUserId(), now);
        userStatusRepository.save(userStatus);

        return createdUser;



    }

    @Override
    public UserServiceResponseDTO.FindUserId findByUserId(UserDTO.FindUserDTO dto) {
        return userRepository.findByUserId(dto.id())
                .map(this::)
                .orElseThrow(() ->
                        new NoSuchElementException("User with id" + dto.id() + "not found"));

//        UserStatus userStatus = userStatusRepository.findByUserId(dto.id())
//                .orElseThrow(() ->
//                        new NoSuchElementException("UserStatus with id " + dto.id() + " not found"));

//        return new UserServiceResponseDTO.FindUserId(
//                user.getUserId(),
//                userStatus.isOnline(),
//                userStatus.getLastUpdatedAt()
//        );

    }

//    @Override
//    public User findByUserId(UUID id) {
//        User user = userRepository.findByUserId(id)
//                .orElseThrow(() ->
//                        new NoSuchElementException("User with id" + id + "not found"));
//
//        UserStatus userStatus = userStatusRepository.findByUserId(id)
//                .orElseThrow(() ->
//                        new NoSuchElementException("UserStatus with id " + id + " not found"));
//
//        return userRepository.findByUserId(id)
//                .orElseThrow(() -> new NoSuchElementException
//                        ("User with id " + id + " not found"));
//    }

    @Override
    public List<UserDTO.FindUserDTO> findAllUser() {

        List<User> users = userRepository.findAllUser();

        return users.stream().map(user -> {
            UserStatus userStatus = userStatusRepository.findByUserId(user.getId())
                    .orElseThrow(() -> new NoSuchElementException("UserStatus with id " + user.getId() + " not found"));

            return new UserDTO.FindUserDTO(
                    user.getId(),
                    userStatus.isOnline(),
                    userStatus.getLastUpdatedAt()
            );
        }).toList();
    }

    @Override
    public User updateUser(UserDTO.updateUserDTO dto) {

//        User user = userRepository.findByUserId(id)
//                .orElseThrow(() -> new NoSuchElementException("User not found"));
//        return userRepository.save(user);
        return userRepository.findByUserId(dto.id())
                .orElseThrow(() ->
                        new NoSuchElementException("User with id" + dto.id() + "not found"));
    }



    @Override
    public boolean deleteUser(UserDTO.deleteDTO dto) {
        Optional<User> optionalUser = userRepository.findByUserId(dto.id());
        if (optionalUser.isEmpty()) {
            return false;
        }

        User user = optionalUser.get();
        userStatusRepository.deleteById(dto.id());

        UUID profileId = user.getProfileId();

        if (profileId != null){
            binaryContentRepository.deleteById(profileId);
        }
        else {
            throw new NoSuchElementException("profileImage not found");
        }
        userRepository.deleteById(dto.id());
        return true;
    }

//    private UserDTO toDto(User user) {
//        Boolean online = userStatusRepository.findByUserId(user.getId())
//                .map(UserStatus::isOnline)
//                .orElse(null);
//
//        return new UserDTO(
//                user.getId(),
//                user.getCreatedAt(),
//                user.getUpdatedAt(),
//                user.getUserName(),
//                user.getEmail(),
//                user.getProfileId(),
//                online
//        );
//    }



}
