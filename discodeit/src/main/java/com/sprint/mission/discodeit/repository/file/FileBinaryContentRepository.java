package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
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
public class FileBinaryContentRepository implements BinaryContentRepository {
    private final Path DIRECTORY;
    private final String EXTENSION = ".ser";

    public FileBinaryContentRepository() {
        this.DIRECTORY = Path.of(System.getProperty("user.dir"),"file-data-map", BinaryContent.class.getSimpleName());
        if (Files.notExists(DIRECTORY)){
            try {
                Files.createDirectories(DIRECTORY);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Path resolvePath(UUID id) {
        return DIRECTORY.resolve(id + EXTENSION);
    }


    @Override
    public BinaryContent create(BinaryContent binaryContent) {
       Path path = resolvePath(binaryContent.getId());
        try (
                FileOutputStream fos = new FileOutputStream(path.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos)

        ) {
            oos.writeObject(binaryContent);
                return binaryContent;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
    }

    @Override
    public BinaryContent save(BinaryContent binaryContent) {
        Path path = resolvePath(binaryContent.getId());
        try (
                FileOutputStream fos = new FileOutputStream(path.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos)
        ) {
            oos.writeObject(binaryContent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return binaryContent;
    }

    @Override
    public boolean existsById(UUID id) {
        Path path = resolvePath(id);
        return Files.exists(path);
    }

    @Override
    public Optional<BinaryContent> findById(UUID id) {
        BinaryContent binaryContent = null;
        Path path = resolvePath(id);
        if (Files.exists(path)) {
            try (
                    FileInputStream fis = new FileInputStream(path.toFile());
                    ObjectInputStream ois = new ObjectInputStream(fis)
            ) {
               binaryContent = (BinaryContent) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        return Optional.ofNullable(Optional.ofNullable(binaryContent)
                .orElseThrow(() -> new NoSuchElementException("BinaryContent with id " + id + " not found")));

    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> ids) {
        try (Stream<Path> paths = Files.list(DIRECTORY)) {
            return paths
                    .filter(path -> path.toString().endsWith(EXTENSION))
                    .map(path -> {
                        try (
                                FileInputStream fis = new FileInputStream(path.toFile());
                                ObjectInputStream ois = new ObjectInputStream(fis)
                        ) {
                            return (BinaryContent) ois.readObject();
                        } catch (IOException | ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .filter(content -> ids.contains(content.getId()))
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

//    @Override
//    public Optional<BinaryContent> findAllByIdIn(Iterable<UUID> ids) {
//        BinaryContent binaryContent = null;
//        Path path = resolvePath(ids.iterator().next());
//        if (Files.exists(path)) {
//            try (
//                    FileInputStream fis = new FileInputStream(path.toFile());
//                    ObjectInputStream ois = new ObjectInputStream(fis)
//            ) {
//                binaryContent = (BinaryContent) ois.readObject();
//            } catch (IOException | ClassNotFoundException e) {
//                throw new RuntimeException(e);
//            }
//        }
//
//        return Optional.ofNullable(Optional.ofNullable(binaryContent)
//                .orElseThrow(() -> new NoSuchElementException("User with id " + ids + " not found")));
//
//    }


    @Override
    public void deleteById(UUID id) {
        Path path = resolvePath(id);
        if (Files.notExists(path)) {
            throw new NoSuchElementException("BinaryContent with id " + id + " not found");
        }
        try {
            Files.delete(path);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
