package com.sprint.mission.discodeit.dto;


public class BinaryContentDTO {

    public record CreateBinaryContentDTO(
            byte[] content, String contentType, String fileName
    ) { }

}
