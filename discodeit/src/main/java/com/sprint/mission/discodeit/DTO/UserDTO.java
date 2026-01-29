package com.sprint.mission.discodeit.DTO;

import com.sprint.mission.discodeit.entity.User;

import java.util.UUID;

public class UserDTO {

    public record CreateUserDTO(
            String userName,
            String email,
            String password
    ) {
        public User toEntity() {
            return new User(userName, email, password);
        }
    }

    public record UpdateUserDTO(
            String userName,
            String email,
            String password
    ) {
        public User toEntity() {
            return new User(userName, email, password);
        }

    }




}
