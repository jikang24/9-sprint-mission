package com.sprint.mission.discodeit.service.binary;

import com.sprint.mission.discodeit.dto.BinaryContentDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
@Repository
public class BinaryContentService {
    private final BinaryContentRepository binaryContentRepository;

    public BinaryContentService(BinaryContentRepository binaryContentRepository) {
        this.binaryContentRepository = binaryContentRepository;
    }

    public BinaryContent createBinaryContent(BinaryContentDTO.CreateBinaryContentDTO dto){

        BinaryContent binaryContent = new BinaryContent(
                dto.content(),
                dto.contentType(),
                dto.fileName()
        );

        return binaryContentRepository.save(binaryContent);
    }

    public Optional<BinaryContent> findByBinaryContentId(UUID binaryContentId){
        return binaryContentRepository.findById(binaryContentId);
    }

    public List<BinaryContent> findAllByIdIn(List<UUID> binaryContentIds){
        return binaryContentRepository.findAllByIdIn(binaryContentIds).stream()
                .toList();
    }

//    public BinaryContent save(BinaryContent binaryContent){
//        return binaryContentRepository.save(binaryContent);
//    }

    public void deleteById(UUID binaryContentId){
        if (!binaryContentRepository.existsById(binaryContentId)) {
            throw new NoSuchElementException("BinaryContent with id " + binaryContentId + " not found");
        }
        binaryContentRepository.deleteById(binaryContentId);
    }


}
