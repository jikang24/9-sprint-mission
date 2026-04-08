package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PublicChannelCreateRequest(
    @NotBlank(message = "채널 이름은 필수입니다")
    @Size(max = 20, message = "이름은 20자 이하여야 합니다")
    String name,

    @Size(max = 30, message = "채널 설명은 30자 이하로만 가능합니다")
    String description
) {

}
