package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@ResponseBody
@RequestMapping("/api/channel")
@RequiredArgsConstructor
@Controller
public class ChannelController {
    private final ChannelService channelService;

    @RequestMapping(
            path = "createPublic",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}
    )
    public ResponseEntity<Channel> publicChannelCreate(
            @RequestPart("PublicChannelCreateRequest") PublicChannelCreateRequest request
    ){
        PublicChannelCreateRequest publicChannelCreateRequest = new PublicChannelCreateRequest(request.name(), request.description());
        Channel publicChannel = channelService.create(publicChannelCreateRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(publicChannel);
    }

    @RequestMapping(
            path = "createPrivate",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}
    )
    public ResponseEntity<Channel> privateChannelCreate(
            @RequestPart("PrivateChannelCreateRequest") PrivateChannelCreateRequest request
    ){
        PrivateChannelCreateRequest privateChannelCreateRequest = new PrivateChannelCreateRequest(request.participantIds());
        Channel privateChannel = channelService.create(privateChannelCreateRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(privateChannel);
    }

    @RequestMapping(
            path = "updatePublic",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}
    )
    public ResponseEntity<Channel> publicChannelUpdate(
            @RequestParam("channelId") UUID channelId,
            @RequestPart("PublicChannelCreateRequest") PublicChannelCreateRequest request
    ){
        PublicChannelUpdateRequest publicChannelUpdateRequest = new PublicChannelUpdateRequest(request.name(), request.description());
        Channel updatedchannel = channelService.update(channelId, publicChannelUpdateRequest);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(updatedchannel);
    }
    @RequestMapping(
            path = "delete",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}
    )
    public ResponseEntity<Void> deleteChannel(
            @RequestParam("channelId") UUID channelId
    ){
        channelService.delete(channelId);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(
            path = "find",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}
    )
    public ResponseEntity<Channel> find(
            @RequestParam("channelId") UUID channelId
    ){
        channelService.find(channelId);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(
            path = "findAllByUserId",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}
    )
    public ResponseEntity<Iterable<Channel>> findAllByUserId(
            @RequestParam("userId") UUID userId
    ){
        channelService.findAllByUserId(userId);
        return ResponseEntity.ok().build();
    }

}
