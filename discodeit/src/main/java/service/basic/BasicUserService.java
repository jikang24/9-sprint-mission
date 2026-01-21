package service.basic;

import entity.User;
import repository.UserRepository;
import service.UserService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

public class BasicUserService implements UserService {
    private final UserRepository userRepository;

    public BasicUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(String username, String email, String phonenumber){
        User user = new User(username, email, phonenumber);
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
    public User updateUser(UUID id, String userName, String email, String phoneNumber) {
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
