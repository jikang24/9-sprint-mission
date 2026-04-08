package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.Size;

public record PublicChannelUpdateRequest(
    @Size(min = 1, max = 20, message = "채널 이름은 1자 이상 20자 이하여야 합니다.")
    String newName,

    @Size(max = 30, message = "채널 설명은 30자 이하로만 가능합니다")
    String newDescription
) {

}
