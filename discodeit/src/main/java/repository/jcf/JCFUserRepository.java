package repository.jcf;

import entity.User;
import repository.UserRepository;

import java.util.*;

public class JCFUserRepository implements UserRepository {
    private final Map<UUID, User> store = new HashMap<>();

    @Override
    public User save (User user) {
        store.put(user.getUserId(), user);
        return user;
    }

    @Override
    public Optional<User> findByUserId(UUID userId) {
        return Optional.ofNullable(store.get(userId));
    }

    @Override
    public List<User> findAllUser() {
        return new ArrayList<>(store.values());
    }
    @Override
    public boolean existsById(UUID userId) {
        return store.containsKey(userId);
    }

    @Override
    public void deleteById(UUID userId) {
        store.remove(userId);
    }


}


