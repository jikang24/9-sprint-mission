package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.UserStatusDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import java.time.Instant;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-11T09:13:54+0900",
    comments = "version: 1.6.3, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.14.4.jar, environment: Java 17.0.17 (Microsoft)"
)
@Component
public class UserStatusMapperImpl implements UserStatusMapper {

    @Override
    public UserStatusDto toDto(UserStatus userStatus) {
        if ( userStatus == null ) {
            return null;
        }

        UUID userId = null;
        UUID id = null;
        Instant lastActiveAt = null;

        userId = userStatusUserId( userStatus );
        id = userStatus.getId();
        lastActiveAt = userStatus.getLastActiveAt();

        UserStatusDto userStatusDto = new UserStatusDto( id, userId, lastActiveAt );

        return userStatusDto;
    }

    private UUID userStatusUserId(UserStatus userStatus) {
        User user = userStatus.getUser();
        if ( user == null ) {
            return null;
        }
        return user.getId();
    }
}
