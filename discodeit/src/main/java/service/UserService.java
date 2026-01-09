package service;

import entity.User;

import java.util.List;

public interface UserService {
    //생성
    User addUser(String displayName, String email, String phoneNumber);


    //조회
    User getUser(String displayName);

    //전체 조회
    List<User> getAllUsers();

    //수정
    User updateUser(String displayName, String email, String phoneNumber);

    //유저이름 -> 전체 목록에서 검색 -> User 객체에서 가져오고 -> 가져온 User안에 필드면 id를 조회해서 리스트에서 검색한다음 삭제하도록 진행

    //삭제
    boolean deleteUser(String displayName);
}
