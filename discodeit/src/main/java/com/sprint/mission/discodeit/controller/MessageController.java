package com.sprint.mission.discodeit.controller;


import com.sprint.mission.discodeit.controller.api.MessageApi;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequestMapping("/api/message")
@RequiredArgsConstructor
@RestController
public class MessageController implements MessageApi {

  private final MessageService messageService;

  @PostMapping(
      path = "create",
      consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}
  )
  @Override
  public ResponseEntity<Message> create(
      @RequestPart("messageCreateRequest") MessageCreateRequest messageCreateRequest,
      @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments
  ) {
    List<BinaryContentCreateRequest> attachmentRequests = Optional.ofNullable(attachments)
        .map(files -> files.stream()
            .map(file -> {
              try {
                return new BinaryContentCreateRequest(
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getBytes()
                );
              } catch (IOException e) {
                throw new RuntimeException(e);
              }
            })
            .toList())
        .orElse(new ArrayList<>());
    Message createdMessage = messageService.create(messageCreateRequest, attachmentRequests);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(createdMessage);
  }


  @PatchMapping(
      path = "update",
      consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}
  )
  @Override
  public ResponseEntity<Message> update(
      @RequestParam("messageId") UUID messageId,
      @RequestPart("message") MessageUpdateRequest messageUpdateRequest,
      @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments
  ) {
    Message updatedMessage = messageService.update(messageId, messageUpdateRequest);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(updatedMessage);
  }


  @DeleteMapping(path = "delete")
  @Override
  public ResponseEntity<Void> delete(
      @RequestParam("messageId") UUID messageId
  ) {
    messageService.delete(messageId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @GetMapping(
      path = "findAllByChannelId"
  )
  @Override
  public ResponseEntity<List<Message>> findAllByChannelId(
      @RequestParam("channelId") UUID channelId
  ) {
    List<Message> messages = messageService.findAllByChannelId(channelId);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(messages);
  }


}
