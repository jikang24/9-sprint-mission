package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-03-14T00:05:25+0900",
    comments = "version: 1.6.3, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.14.4.jar, environment: Java 17.0.17 (Microsoft)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Autowired
    private BinaryContentMapper binaryContentMapper;

    @Override
    public UserDto toDto(User user) {
        if ( user == null ) {
            return null;
        }

        BinaryContentDto profile = null;
        Boolean online = null;
        UUID id = null;
        String username = null;
        String email = null;

        profile = binaryContentMapper.toDto( user.getProfile() );
        online = userStatusOnline( user );
        id = user.getId();
        username = user.getUsername();
        email = user.getEmail();

        UserDto userDto = new UserDto( id, username, email, profile, online );

        return userDto;
    }

    private Boolean userStatusOnline(User user) {
        UserStatus status = user.getStatus();
        if ( status == null ) {
            return null;
        }
        return status.isOnline();
    }
}
