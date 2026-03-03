package com.sprint.mission.discodeit.controller.api;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
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
      ),
      @ApiResponse(
          responseCode = "404", description = "BinaryContent를 찾을 수 없음",
          content = @Content(examples = @ExampleObject(value = "BinaryContent with id {binaryContentId} not found"))
      )
  })
  ResponseEntity<BinaryContent> find(
      @Parameter(description = "BinaryContent 조회할 첨부 파일 ID") UUID binaryContentId
  );

  @Operation(summary = "BinaryContent 여러 첨부 파일 조회")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200", description = "BinaryContent 전체 조회",
          content = @Content(array = @ArraySchema(schema = @Schema(implementation = BinaryContent.class)))
      )
  })
  ResponseEntity<List<BinaryContent>> findAllByIdIn(
      @Parameter(description = "조회할 첨부 파일 ID 목록") List<UUID> binaryContentIds
  );
}
