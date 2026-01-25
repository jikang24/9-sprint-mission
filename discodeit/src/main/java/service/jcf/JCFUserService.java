package service.jcf;

import entity.User;
import service.Exception.UserCrudException;
import service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JCFUserService implements UserService {
    private final List<User> data = new ArrayList<>();


    //생성 오버라이드
    @Override
    public User createUser(String userName, String email, String phoneNumber) {
        User user = new User(userName, email, phoneNumber);
        data.add(user);
        return user;
    }

    //조회 오버라이드
    @Override
    public User findByUserId(UUID id) {
        for (User user : data) {
            if (user.getUserId().equals(id)) {
                return user;
            }
        }
        throw new RuntimeException("유저를 찾을 수 없습니다.");
    }

    // 전체조회 오버라이드
    @Override
    public List<User> findAllUser() {
        if (data.isEmpty()) {
           System.out.println("유저가 없습니다.");
        }
        return data;
    }

    @Override
    public User updateUser(UUID id, String userName, String email, String phoneNumber) {

        try {
            User user = this.data.stream().filter
                    (u -> u.getUserId().equals(id)).findFirst().orElse(null);

            if (user == null) {
                throw new RuntimeException("해당 Id를 가진 유저가 없습니다.");
            }

            boolean updated = false;

            if (userName != null) {
                user.updateUserName(userName);
                updated = true;
            }

            if (email != null) {
                user.updateEmail(email);
                updated = true;
            }

            if (phoneNumber != null) {
                user.updatePhoneNumber(phoneNumber);
                updated = true;
            }

            if (!updated) {
                throw new RuntimeException("수정할 값이 없습니다.");
            }

            return user;
        } catch (UserCrudException e) {
            System.out.println("User에서 다음과 같은 에러가 발생했습니다: " + e.getMessage());
            throw e;
        }

    }

    @Override
    public boolean deleteUser(UUID id) {
        User user = findByUserId(id);
        if (user == null) {
            System.out.println("삭제할 유저가 없습니다.");
            return false;
        }
        return data.remove(user);
    }
}