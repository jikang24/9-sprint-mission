package com.sprint.mission.discodeit.service.auth;

import com.sprint.mission.discodeit.dto.UserDTO;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class AuthService {
   private final UserRepository userRepository;


    public User login(UserDTO.loginDTO dto){
        User user = userRepository.findByUserName(dto.userName())
                .orElseThrow(() ->
                        new NoSuchElementException("로그인 정보를 다시 확인해주세요")
                );

        if (!user.getPassword().equals(dto.password())) {
            throw new NoSuchElementException("로그인 정보를 다시 확인해주세요");
        }

        return user;

    }






}
