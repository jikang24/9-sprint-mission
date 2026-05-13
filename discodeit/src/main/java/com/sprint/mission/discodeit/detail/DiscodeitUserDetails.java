package com.sprint.mission.discodeit.detail;

import com.sprint.mission.discodeit.dto.data.UserDto;
import java.util.Collection;
import java.util.List;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@EqualsAndHashCode(of = "userDto")
@Getter
@RequiredArgsConstructor
public class DiscodeitUserDetails implements UserDetails {

  private final UserDto userDto;
  private final String password;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of();
  }

  @Override
  public String getUsername() {
    return userDto.username();
  }

  @Override
  public String getPassword() {
    return password;
  }

}
