package com.sprint.mission.discodeit.controller;


import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.UUID;

@ResponseBody
@RequestMapping("/api/binaryContent")
@RequiredArgsConstructor
@Controller
public class BinaryContentController {
    private final BinaryContentService binaryContentService;

    @RequestMapping(
            path = "find",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}
    )
    public ResponseEntity<BinaryContent> find(
            @RequestParam("binaryContentId") UUID binaryContentId
    ){
        BinaryContent binaryContent = binaryContentService.find(binaryContentId);
        return ResponseEntity.ok(binaryContent);
    }

    @RequestMapping(
            path = "findAll",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}
    )
    public ResponseEntity<List<BinaryContent>> findAllByIdIn(
            @RequestParam("binaryContentIds") List<UUID> binaryContentIds
    ){
        List<BinaryContent> binaryContents = binaryContentService.findAllByIdIn(binaryContentIds);
        return ResponseEntity.ok(binaryContents);
    }
}
