package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Binary Content", description = "Binary Content API")
public interface BinaryContentApi {

  @Operation(summary = "BinaryContent 조회")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "BinaryContent 단건 조회 완료",
          content = @Content(schema = @Schema(implementation = BinaryContentApi.class))
      )
  })
  ResponseEntity<BinaryContent> findByIdIn(
      @Parameter(
          description = "BinaryContent 단건 정보",
          content = @Content(schema = @Schema(implementation = BinaryContentCreateRequest.class))
      ) BinaryContentCreateRequest request
  );

  @Operation(summary = "BinaryContent 전체 조회")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "BinaryContent 전체 조회",
          content = @Content(array = @ArraySchema(schema = @Schema(implementation = BinaryContentApi.class)))
      )
  })
  ResponseEntity<List<BinaryContent>> findAllByIdIn(
      @RequestParam("binaryContentId") UUID binaryContentId
  );
}
