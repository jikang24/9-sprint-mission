package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.request.LoginRequest;
import com.sprint.mission.discodeit.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Auth", description = "Auth API")
public interface AuthApi {

  @Operation(summary = "Login 등록")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "Login이 성공적으로 진행됨",
          content = @Content(schema = @Schema(implementation = User.class))
      ),
      @ApiResponse(
          responseCode = "401", description = "Login 인증에 실패함",
          content = @Content(schema = @Schema(implementation = User.class))
      )
  })
  ResponseEntity<User> login(
      @Parameter(
          description = "Login 정보",
          required = true
      ) LoginRequest loginRequest
  );

}
