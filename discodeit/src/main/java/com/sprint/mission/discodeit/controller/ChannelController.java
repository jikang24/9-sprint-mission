package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.api.ChannelApi;
import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/channels")
public class ChannelController implements ChannelApi {

  private final ChannelService channelService;

  @PostMapping(path = "public")
  @Override
  public ResponseEntity<Channel> create(@RequestBody PublicChannelCreateRequest request) {
    Channel publicChannel = channelService.create(request);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(publicChannel);
  }

  @PostMapping(path = "private")
  @Override
  public ResponseEntity<Channel> privateChannelCreate(
      @RequestBody PrivateChannelCreateRequest request) {
    Channel privateChannel = channelService.create(request);
    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(privateChannel);
  }

  @PatchMapping(path = "{channelId}")
  @Override
  public ResponseEntity<Channel> publicChannelUpdate(
      @PathVariable("channelId") UUID channelId,
      @RequestBody PublicChannelUpdateRequest request) {
    Channel updatedchannel = channelService.update(channelId, request);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(updatedchannel);
  }

  @DeleteMapping(path = "{channelId}")
  @Override
  public ResponseEntity<Void> delete(@PathVariable("channelId") UUID channelId) {
    channelService.delete(channelId);
    return ResponseEntity
        .status(HttpStatus.NO_CONTENT)
        .build();
  }


  @GetMapping(path = "{channelId}")
  @Override
  public ResponseEntity<ChannelDto> find(@PathVariable("channelId") UUID channelId) {
    if (channelId == null) {
      return ResponseEntity.badRequest().build();
    }
    ChannelDto channel = channelService.find(channelId);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(channel);
  }


  @GetMapping(path = "{userId}")
  @Override
  public ResponseEntity<List<ChannelDto>> findAllByUserId(
      @PathVariable("userId") UUID userId
  ) {
    if (userId == null) {
      return ResponseEntity.badRequest().build();
    }
    List<ChannelDto> channel = channelService.findAllByUserId(userId);
    return ResponseEntity
        .status(HttpStatus.OK)
        .body(channel);
  }

}

