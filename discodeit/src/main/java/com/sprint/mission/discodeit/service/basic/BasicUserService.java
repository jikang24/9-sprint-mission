package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.DTO.UserDTO;
import com.sprint.mission.discodeit.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;

//    public BasicUserService(UserRepository userRepository) {
//        this.userRepository = userRepository;
//    }

    @Override
    public User createUser(String username, String email, String password){
        User user = new User(username, email, password);
        return userRepository.save(user);
    }

    @Override
    public User createUser(UserDTO.CreateUserDTO createUserDTO) {
        User user = createUserDTO.toEntity();
        return userRepository.save(user);
    }

    @Override
    public User findByUserId(UUID id) {
        return userRepository.findByUserId(id)
                .orElseThrow(() -> new NoSuchElementException
                        ("User with id " + id + " not found"));
    }

    @Override
    public List<User> findAllUser() {
        return userRepository.findAllUser();
    }

    @Override
    public User updateUser(UUID id, String userName, String email, String password) {
        User user = userRepository.findByUserId(id)
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        return userRepository.save(user);
    }

    @Override
    public boolean deleteUser(UUID id) {
        if (userRepository.existsById(id)) {
            throw new NoSuchElementException("User not found");
        }
         userRepository.deleteById(id);
         return true;
    }


}
