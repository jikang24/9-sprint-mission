package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.secure.handler.LoginFailureHandler;
import com.sprint.mission.discodeit.secure.handler.LoginSuccessHandler;
import com.sprint.mission.discodeit.secure.handler.SpaCsrfTokenRequestHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final LoginSuccessHandler loginSuccessHandler;
  private final LoginFailureHandler loginFailureHandler;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .csrf(csrf -> csrf
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            .csrfTokenRequestHandler(new SpaCsrfTokenRequestHandler())
        )
        .formLogin(login -> login
            .loginProcessingUrl("/api/auth/login")
            .successHandler(loginSuccessHandler)
            .failureHandler(loginFailureHandler)
            .permitAll()
        )
        .logout(logout -> logout
            .logoutUrl("/api/auth/logout")
            .logoutSuccessHandler(
                new HttpStatusReturningLogoutSuccessHandler(HttpStatus.NO_CONTENT))
            .invalidateHttpSession(true)
            .deleteCookies("JSESSIONID")
            .permitAll()
        )
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
            .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
            .requestMatchers("/api/auth/**").permitAll()
            .requestMatchers("/api/channels").hasAnyRole("ADMIN", "CHANNEL_MANAGER")
            .requestMatchers("/api/messages").hasRole("ADMIN")
            .anyRequest()
            .authenticated()
        );

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }


}
