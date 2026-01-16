package service;

import entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    //생성
    User createUser(String userName, String email, String phoneNumber);
    //조회
    User findByUserId(UUID id);

    //전체 조회
    List<User> findAllUser();

    //수정
    User updateUser(UUID id, String userName, String email, String phoneNumber);
    //삭제
    boolean deleteUser(UUID id);
}
