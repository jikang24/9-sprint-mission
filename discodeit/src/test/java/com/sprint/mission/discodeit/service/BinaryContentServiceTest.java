package com.sprint.mission.discodeit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.basic.BasicBinaryContentService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class BinaryContentServiceTest {

  @InjectMocks
  private BasicBinaryContentService binaryContentService;

  @Mock
  private BinaryContentRepository binaryContentRepository;
  @Mock
  private BinaryContentMapper binaryContentMapper;
  @Mock
  private BinaryContentStorage binaryContentStorage;

  @Test
  @DisplayName("파일 업로드 성공")
  void create_success() {
    byte[] bytes = "testContent".getBytes();
    BinaryContentCreateRequest request = new BinaryContentCreateRequest(
        "test.txt", "text/plain", bytes
    );

    BinaryContent mockContent = new BinaryContent("test.txt", (long) bytes.length, "text/plain");
    BinaryContentDto mockDto = new BinaryContentDto(
        UUID.randomUUID(), "test.txt", (long) bytes.length, "text/plain"
    );

    given(binaryContentRepository.save(any(BinaryContent.class))).willReturn(mockContent);
    given(binaryContentMapper.toDto(any(BinaryContent.class))).willReturn(mockDto);

    BinaryContentDto result = binaryContentService.create(request);

    assertThat(result.fileName()).isEqualTo("test.txt");
    assertThat(result.contentType()).isEqualTo("text/plain");
    assertThat(result.size()).isEqualTo((long) bytes.length);
    then(binaryContentRepository).should().save(any(BinaryContent.class));
    then(binaryContentStorage).should().put(any(), any());
  }

  @Test
  @DisplayName("파일 업로드 실패 - 빈 파일")
  void create_fail_emptyBytes() {
    BinaryContentCreateRequest request = new BinaryContentCreateRequest(
        "test.txt", "text/plain", new byte[0]
    );
    assertThatThrownBy(() -> binaryContentService.create(request))
        .isInstanceOf(IllegalArgumentException.class);

    then(binaryContentRepository).shouldHaveNoInteractions();
    then(binaryContentStorage).shouldHaveNoInteractions();
  }

  @Test
  @DisplayName("파일 단건 조회 성공")
  void find_success() {
    UUID id = UUID.randomUUID();
    BinaryContent mockContent = new BinaryContent("test.txt", 11L, "text/plain");
    BinaryContentDto mockDto = new BinaryContentDto(id, "test.txt", 11L, "text/plain");

    given(binaryContentRepository.findById(id)).willReturn(Optional.of(mockContent));
    given(binaryContentMapper.toDto(mockContent)).willReturn(mockDto);

    BinaryContentDto result = binaryContentService.find(id);

    assertThat(result.id()).isEqualTo(id);
    assertThat(result.fileName()).isEqualTo("test.txt");
    then(binaryContentRepository).should().findById(id);
  }

  @Test
  @DisplayName("파일 단건 조회 실패 - 존재하지 않는 ID")
  void find_fail_notFound() {
    UUID id = UUID.randomUUID();

    given(binaryContentRepository.findById(id)).willReturn(Optional.empty());

    assertThatThrownBy(() -> binaryContentService.find(id))
        .isInstanceOf(NoSuchElementException.class);
  }

  @Test
  @DisplayName("파일 다건 조회 성공")
  void findAllByIdIn_success() {
    UUID id1 = UUID.randomUUID();
    UUID id2 = UUID.randomUUID();
    List<UUID> ids = List.of(id1, id2);

    BinaryContent mockContent1 = new BinaryContent("file1.txt", 5L, "text/plain");
    BinaryContent mockContent2 = new BinaryContent("file2.txt", 7L, "text/plain");
    BinaryContentDto mockDto1 = new BinaryContentDto(id1, "file1.txt", 5L, "text/plain");
    BinaryContentDto mockDto2 = new BinaryContentDto(id2, "file2.txt", 7L, "text/plain");

    given(binaryContentRepository.findAllById(ids)).willReturn(List.of(mockContent1, mockContent2));
    given(binaryContentMapper.toDto(mockContent1)).willReturn(mockDto1);
    given(binaryContentMapper.toDto(mockContent2)).willReturn(mockDto2);

    List<BinaryContentDto> result = binaryContentService.findAllByIdIn(ids);

    assertThat(result).hasSize(2);
    assertThat(result).extracting(BinaryContentDto::fileName)
        .containsExactly("file1.txt", "file2.txt");
  }

  @Test
  @DisplayName("파일 삭제 성공")
  void delete_success() {
    UUID id = UUID.randomUUID();

    given(binaryContentRepository.existsById(id)).willReturn(true);

    binaryContentService.delete(id);

    then(binaryContentRepository).should().deleteById(id);
  }

  @Test
  @DisplayName("파일 삭제 실패 - 존재하지 않는 ID")
  void delete_fail_notFound() {
    UUID id = UUID.randomUUID();

    given(binaryContentRepository.existsById(id)).willReturn(false);

    assertThatThrownBy(() -> binaryContentService.delete(id))
        .isInstanceOf(NoSuchElementException.class);

    then(binaryContentRepository).should(never()).deleteById(any());
  }
}
