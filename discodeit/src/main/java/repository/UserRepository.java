package repository;

import entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    User save (User user);

    Optional<User> findByUserId(UUID id);

    List<User> findAllUser();

    boolean existsById(UUID id);

    void deleteById(UUID id);
}
