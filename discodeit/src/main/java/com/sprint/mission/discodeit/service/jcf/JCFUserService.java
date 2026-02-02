package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.DTO.UserDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.Exception.UserCrudException;
import com.sprint.mission.discodeit.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class JCFUserService implements UserService {
    private final List<User> data = new ArrayList<>();


    //생성 오버라이드
    @Override
    public User createUser(String userName, String email, String password) {
        User user = new User(userName, email, password);
        data.add(user);
        return user;
    }

    //조회 오버라이드
    @Override
    public UserDTO.FindUserDTO findByUserId(UUID id) {
        return data.stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElseThrow(RuntimeException.class);
    }

    // 전체조회 오버라이드
    @Override
    public Stream<UserDTO.FindUserDTO> findAllUser() {
        if (data.isEmpty()) {
           System.out.println("유저가 없습니다.");
        }
        return data;
    }

    @Override
    public UserDTO.UpdateUserDTO updateUser(UUID id, String userName, String email, String password) {

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

            if (password != null) {
                user.updatePassword(password);
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