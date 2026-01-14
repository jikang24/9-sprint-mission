package service;

import entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    //생성
    User create(String username, String email, String phonenumber);
    //조회
    User find(UUID id);

    //전체 조회
    List<User> findAll();

    //수정
    User update(UUID id, String username, String email, String phonenumber);
    //삭제
    boolean delete(UUID id);
}
