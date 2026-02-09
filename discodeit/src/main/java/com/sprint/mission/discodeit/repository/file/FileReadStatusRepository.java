package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.status.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
@Repository
public class FileReadStatusRepository implements ReadStatusRepository {
    private final Path DIRECTORY;
    private final String EXTENSION = ".ser";

    public FileReadStatusRepository() {
        this.DIRECTORY = Path.of(System.getProperty("user.dir"), "file-data-map", ReadStatus.class.getSimpleName());
        if (Files.notExists(DIRECTORY)){
            try {
                Files.createDirectories(DIRECTORY);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Path resolvePath(UUID id) {
        return DIRECTORY.resolve(id + EXTENSION);
    }


    @Override
    public ReadStatus read(User userId, Channel channelId) {
        Path path = resolvePath(userId.getUserId());
        try (
                FileOutputStream fos = new FileOutputStream(path.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos)
                ){
            oos.writeObject(userId);
        }catch (IOException e){
            throw new RuntimeException(e);
        }
       return null;
    }

    @Override
    public Optional<ReadStatus> findByUserIdAndChannelId(UUID userId, UUID channelId) {
        ReadStatus readStatus = null;
        Path path = resolvePath(userId);
        if (Files.exists(path)) {
            try (
                    FileInputStream fis = new FileInputStream(path.toFile());
                    ObjectInputStream ois = new ObjectInputStream(fis)
            ) {
                readStatus = (ReadStatus) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return Optional.ofNullable(readStatus);
    }

    @Override
    public Optional<ReadStatus> findById(UUID id) {
        ReadStatus readStatusNullable = null;
        Path path = resolvePath(id);
        if (Files.exists(path)) {
            try (
                    FileInputStream fis = new FileInputStream(path.toFile());
                    ObjectInputStream ois = new ObjectInputStream(fis)
            ) {
                readStatusNullable = (ReadStatus) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return Optional.ofNullable(readStatusNullable);
    }

    @Override
    public List<ReadStatus> findAllByChannelId(UUID channelId) {
        try (Stream<Path> paths = Files.list(DIRECTORY)) {
            return paths
                    .filter(path -> path.toString().endsWith(EXTENSION))
                    .map(path -> {
                        try (
                                FileInputStream fis = new FileInputStream(path.toFile());
                                ObjectInputStream ois = new ObjectInputStream(fis)
                        ) {
                            return (ReadStatus) ois.readObject();
                        } catch (IOException | ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .filter(readStatus -> readStatus.getChannelId().equals(channelId))
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        try {
            return Files.list(DIRECTORY)
                    .filter(path -> path.toString().endsWith(EXTENSION))
                    .map(path -> {
                        try (
                                FileInputStream fis = new FileInputStream(path.toFile());
                                ObjectInputStream ois = new ObjectInputStream(fis)
                        ) {
                            return (ReadStatus) ois.readObject();
                        } catch (IOException | ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    }).toList();

        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public ReadStatus update(ReadStatus readStatus) {
        Path path = resolvePath(readStatus.getUserId());
        if (Files.exists(path)) {
            try (
                    FileOutputStream fos = new FileOutputStream(path.toFile());
                    ObjectOutputStream oos = new ObjectOutputStream(fos)
            ) {
                oos.writeObject(readStatus);
                return readStatus;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new NoSuchElementException("ReadStatus with id " + readStatus.getUserId() + " not found");
        }

    }

    @Override
    public ReadStatus save(ReadStatus readStatus) {
        Path path = resolvePath(readStatus.getUserId());
        if (!Files.exists(path)) {
            try (
                    FileOutputStream fos = new FileOutputStream(path.toFile());
                    ObjectOutputStream oos = new ObjectOutputStream(fos)
            ) {
                oos.writeObject(readStatus);
                return readStatus;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        throw new IllegalStateException("ReadStatus with id " + readStatus.getUserId() + " already exists");
    }

    @Override
    public boolean existsById(UUID messageId) {
        ReadStatus readStatus = null;
        Path path = resolvePath(messageId);
        if (Files.exists(path)) {
            try (
                    FileInputStream fis = new FileInputStream(path.toFile());
                    ObjectInputStream ois = new ObjectInputStream(fis)
            ) {
                readStatus = (ReadStatus) ois.readObject();
            } catch (IOException | ClassNotFoundException e){
                throw new RuntimeException(e);
            }
        } else {
            throw new NoSuchElementException("ReadStatus with id " + readStatus.getUserId() + " not found");
        }
        return readStatus != null;
    }

    @Override
    public void deleteById(UUID id) {
        Path path = resolvePath(id);
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteAllByChannelId(UUID channelId) {
        this.findAllByChannelId(channelId)
                .forEach(readStatus -> this.deleteById(readStatus.getId()));
    }

    @Override
    public ReadStatus delete(ReadStatus readStatus) {
        Path path = resolvePath(readStatus.getUserId());
        if (Files.notExists(path)) {
            throw new NoSuchElementException("ReadStatus with id " + readStatus.getUserId() + " not found");
        }
        try {
            Files.delete(path);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return readStatus;
    }

}
