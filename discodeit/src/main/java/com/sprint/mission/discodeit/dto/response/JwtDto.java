package com.sprint.mission.discodeit.dto.response;

import com.sprint.mission.discodeit.dto.data.UserDto;
import lombok.Builder;

@Builder
public record JwtDto(
    UserDto userDto,
    String accessToken
) {

}
