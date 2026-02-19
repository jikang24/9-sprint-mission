package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Channel", description = "Channel API")
public interface ChannelApi {

//  @Operation(summary = "Channel 등록")
//  @ApiResponse(value = {
//      @ApiResponse(
//          responseCode = "201", description = "Channel이 성공적으로 생성됨",
//          content = @Content(schema = @Schema(implementation = Channel.class))
//      ),
//      @ApiResponse(
//          responseCode = "400", description = "같은 Channelname을 사용하는 Channel이 이미 존재함",
//          content = @Content(examples = @ExampleObject(value = "Channel with name {name} already exists"))
//      ),
//  })
//  ResponseEntity<Channel> create(
//
//  )

}
