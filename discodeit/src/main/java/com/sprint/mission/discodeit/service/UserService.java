package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.BinaryContentDTO;
import com.sprint.mission.discodeit.dto.UserDTO;
import com.sprint.mission.discodeit.dto.UserServiceResponseDTO;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    //생성
//    User createUser(String username,String email,String password);
    User createUser(UserDTO.CreateUserDTO userCreateRequest, Optional<BinaryContentDTO.CreateBinaryContentDTO> profileCreateRequest);
    //조회
//    User findByUserId(UUID id);
    UserServiceResponseDTO.FindUserId findByUserId(UserDTO.FindUserDTO dto);

    //전체 조회
    List<UserDTO.FindUserDTO> findAllUser();

    //수정
    User updateUser(UserDTO.updateUserDTO dto);
    //삭제
    boolean deleteUser(UserDTO.deleteDTO dto);

}
