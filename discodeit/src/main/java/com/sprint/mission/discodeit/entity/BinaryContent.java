package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "binary_contents")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BinaryContent extends BaseUpdatableEntity {

  @Column(columnDefinition = "uuid", updatable = false, nullable = false)
  private UUID id;
  //
  private String fileName;
  private Long size;
  private String contentType;
  private byte[] bytes;

  public BinaryContent(String fileName, Long size, String contentType) {
    this.id = UUID.randomUUID();
    //
    this.fileName = fileName;
    this.size = size;
    this.contentType = contentType;
    this.bytes = bytes;


  }
}
