package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.DTO.UserDTO;
import com.sprint.mission.discodeit.DTO.UserServiceResponseDTO;
import com.sprint.mission.discodeit.entity.User;

import java.util.UUID;
import java.util.stream.Stream;

public interface UserService {
    //생성
//    User createUser(String username,String email,String password);
    User createUser(UserDTO.CreateUserDTO dto);
    //조회
//    User findByUserId(UUID id);
    UserServiceResponseDTO.FindUserId findByUserId(UserDTO.FindUserDTO dto);

    //전체 조회
    Stream<UserDTO.FindUserDTO> findAllUser();

    //수정
    User updateUser(UUID id, String userName, String email, String password);
    //삭제
    boolean deleteUser(UserDTO.FindUserDTO id);

}
