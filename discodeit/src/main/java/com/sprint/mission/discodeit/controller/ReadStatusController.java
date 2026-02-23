package com.sprint.mission.discodeit.controller;


import com.sprint.mission.discodeit.controller.api.ReadStatusApi;
import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v2/readStatus")
@RequiredArgsConstructor
@RestController
public class ReadStatusController implements ReadStatusApi {

  private final ReadStatusService readStatusService;

  @PostMapping(path = "create")
  @Override
  public ResponseEntity<ReadStatus> create(
      @RequestBody ReadStatusCreateRequest readStatusCreateRequest
  ) {
    ReadStatus createdReadStatusFromChannel
        = readStatusService.create(readStatusCreateRequest);
    return ResponseEntity.
        status(HttpStatus.CREATED)
        .body(createdReadStatusFromChannel);
  }


  @PatchMapping(path = "update")
  @Override
  public ResponseEntity<ReadStatus> update(
      @RequestParam("readStatusId") UUID readStatusId,
      @RequestBody ReadStatusUpdateRequest readStatusUpdateRequest
  ) {
    ReadStatus updatedReadStatusFromChannel
        = readStatusService.update(readStatusId, readStatusUpdateRequest);
    return ResponseEntity.
        status(HttpStatus.OK)
        .body(updatedReadStatusFromChannel);
  }


  @GetMapping(path = "findById")
  @Override
  public ResponseEntity<ReadStatus> findById(
      @RequestParam("readStatusId") UUID readStatusId
  ) {
    ReadStatus readStatus = readStatusService.find(readStatusId);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(readStatus);
  }

  @GetMapping(path = "findAllByUserId")
  @Override
  public ResponseEntity<List<ReadStatus>> findAllByUserId(
      @RequestParam("userId") UUID userId
  ) {
    List<ReadStatus> readStatus = readStatusService.findAllByUserId(userId);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(readStatus);
  }
}
