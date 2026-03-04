package com.sprint.mission.discodeit.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_statuses")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserStatus extends BaseUpdatableEntity {

  //  private UUID id;
  @JsonBackReference
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;


  @Column(nullable = false)
  private Instant lastActiveAt;

  public UserStatus(User user, Instant lastActiveAt) {
//    this.id = UUID.randomUUID();
    this.user = user;
    this.lastActiveAt = lastActiveAt;
  }


  public void update(Instant lastActiveAt) {
//    boolean anyValueUpdated = false;
    if (lastActiveAt != null && !lastActiveAt.equals(this.lastActiveAt)) {
      this.lastActiveAt = lastActiveAt;
//      anyValueUpdated = true;
    }

//    if (anyValueUpdated) {
//      this.updatedAt = Instant.now();
//    }
  }

  public Boolean isOnline() {
    Instant instantFiveMinutesAgo = Instant.now().minus(Duration.ofMinutes(5));

    return lastActiveAt.isAfter(instantFiveMinutesAgo);
  }
}
