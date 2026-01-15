package service.jcf;

import entity.User;
import service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFUserService implements UserService {
    private final List<User> data = new ArrayList<>();


    //생성 오버라이드
    @Override
    public User create(String username, String email, String phonenumber) {
        User user = new User(username, email, phonenumber);
        data.add(user);
        return user;
    }

    //조회 오버라이드
    @Override
    public User find(UUID id) {
        for (User user : data) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        throw new RuntimeException("User not found");
    }

    // 전체조회 오버라이드
    @Override
    public List<User> findAll() {
        if (data.isEmpty()) {
           System.out.println("유저가 없습니다.");
        }
        return data;
    }

    @Override
    public User update(UUID id, String username, String email, String phonenumber) {
        for (User user : data) {
            if (user.getId().equals(id)) {
                user.updateUser(username, email, phonenumber);
                return user;
            }
        }
        if (find(id) == null) {
            throw new RuntimeException("해당 Id를 가진 유저가 없습니다");
        }
        throw new RuntimeException("수정할 수 없습니다.");
    }

    @Override
    public boolean delete(UUID id) {
        User user = find(id);
        if (user == null) {
            System.out.println("삭제할 유저가 없습니다.");
            return false;
        }
        data.remove(user);
        return true;
    }
}