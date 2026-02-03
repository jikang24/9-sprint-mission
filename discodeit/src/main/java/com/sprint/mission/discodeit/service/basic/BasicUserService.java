package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.DTO.UserDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.status.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final UserStatusRepository userStatusRepository;

    @Override
    public User createUser(String username, String email, String password){
        User user = new User(username, email, password,null);
        return userRepository.save(user);
    }

    @Override
    public User createUser(UserDTO.CreateUserDTO dto) {
        if(userRepository.existsByEmail(dto.email())){
            throw new IllegalArgumentException("이미 존재하는 email 입니다: " + dto.email());
        }
        if(userRepository.existsByUserName(dto.userName())){
            throw new IllegalArgumentException("이미 존재하는 username 입니다: " + dto.userName());
        }

        User user = new User(
                dto.userName(),
                dto.email(),
                dto.password(),
                dto.profileImage()
        );

        UserStatus userStatus = new UserStatus(user.getUserId());

        userStatusRepository.save(userStatus);
        userRepository.save(user);
        return user;
    }

    @Override
    public UserDTO.FindUserDTO findByUserId(UUID id) {
        User user = userRepository.findByUserId(id)
                .orElseThrow(() ->
                        new NoSuchElementException("User with id" + id + "not found"));

        UserStatus userStatus = userStatusRepository.findByUserId(id)
                .orElseThrow(() ->
                        new NoSuchElementException("UserStatus with id " + id + " not found"));

        return new UserDTO.FindUserDTO(
                user.getId(),
                userStatus.isOnline(),
                userStatus.getLastUpdatedAt()
        );

//        return userRepository.findByUserId(id)
//                .orElseThrow(() -> new NoSuchElementException
//                        ("User with id " + id + " not found"));
    }

    @Override
    public Stream<UserDTO.FindUserDTO> findAllUser() {

        List<User> users = userRepository.findAllUser();

        return users.stream()
                .map(user -> {
                    UserStatus userStatus = userStatusRepository.findByUserId(user.getId())
                            .orElseThrow(() -> new NoSuchElementException("UserStatus with id " + user.getId() + " not found"));

                    return new UserDTO.FindUserDTO(
                            user.getId(),
                            userStatus.isOnline(),
                            userStatus.getLastUpdatedAt()
                    );

                });

    }

    @Override
    public UserDTO.UpdateUserDTO updateUser(UUID id, String userName, String email, String password) {

//        User user = userRepository.findByUserId(id)
//                .orElseThrow(() -> new NoSuchElementException("User not found"));
//        return userRepository.save(user);
        User user = userRepository.findByUserId(id)
                .orElseThrow(() ->
                        new NoSuchElementException("User with id" + id + "not found"));

        return new UserDTO.UpdateUserDTO(
                user.getUserName(),
                user.getEmail(),
                user.getPassword(),
                user.getProfileImage()
        );
    }

    @Override
    public boolean deleteUser(UUID id) {
        Optional<User> optionalUser = userRepository.findByUserId(id);
        if (optionalUser.isEmpty()) {
            return false;
        }

        User user = optionalUser.get();
        userStatusRepository.deleteById(id);

        UUID profileId = user.getProfileId();

        if (profileId != null){
            binaryContentRepository.deleteById(profileId);
        }
        else {
            throw new NoSuchElementException("profileImage not found");
        }
        userRepository.deleteById(id);
        return true;
    }


}
