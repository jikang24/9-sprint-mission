package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PreRemove;
import jakarta.persistence.Table;
import java.io.File;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Entity
@Table(name = "binary_contents")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BinaryContent extends BaseEntity {

  private String filePath;

  @Column(name = "file_name", nullable = false)
  private String fileName;
  @Column(nullable = false)
  private Long size;
  @Column(name = "content_type", length = 100, nullable = false)
  private String contentType;

  public BinaryContent(String fileName, Long size, String contentType) {
    this.fileName = fileName;
    this.size = size;
    this.contentType = contentType;
  }

  @PreRemove
  public void deleteFile() {
    if (filePath == null) return;
    File file = new File(filePath);
    if (file.exists() && !file.delete()) {
      log.warn("Failed to delete file: {}", filePath);
    }
  }
}
