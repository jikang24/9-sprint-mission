package repository.file;

import entity.User;
import repository.UserRepository;

import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

public class FileUserRepository implements Serializable, UserRepository {
    private static final long serialVersionUID = 1L;

    private final Path DIRECTORY;
    private final String EXTENSION = ".ser";

    public FileUserRepository() {
        this.DIRECTORY = Path.of(System.getProperty("user.dir"), "file-data-map", User.class.getSimpleName());
        if (Files.notExists(DIRECTORY)) {
            try {
                Files.createDirectories(DIRECTORY);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

//        System.out.println(DIRECTORY);
    }

    private Path resolvePath(UUID id) {
        return DIRECTORY.resolve(id + EXTENSION);
    }

    @Override
    public User save(User user) {
        User result = new FileUserRepository()
                .findByUserId(user.getUserId()).orElse(null);
        if (result == null) {
            try {
                Files.writeString(resolvePath(user.getUserId()),
                        user.toString());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }
    //????

    @Override
    public Optional<User> findByUserId(UUID id) {
        return Optional.empty();
    }

    @Override
    public List<User> findAllUser() {
        return List.of();
    }

    @Override
    public boolean existsById(UUID id) {
        return Files.exists(resolvePath(id));
    }

    @Override
    public void deleteById(UUID id) {
        Path path = resolvePath(id);
        if (Files.notExists(path)) {
            throw new NoSuchElementException("User with id " + id + " not found");
        }
        try {
            Files.delete(path);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }





}
