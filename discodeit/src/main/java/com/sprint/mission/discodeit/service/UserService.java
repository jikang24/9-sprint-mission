package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.DTO.UserDTO;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    //생성
    User createUser(UserDTO userDTO);
    //조회
    User findByUserId(UUID id);

    //전체 조회
    List<User> findAllUser();

    //수정
    User updateUser(UUID id, String userName, String email, String password);
    //삭제
    boolean deleteUser(UUID id);
}
