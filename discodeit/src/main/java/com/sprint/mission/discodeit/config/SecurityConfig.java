package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.secure.DiscodeitUserDetailsService;
import com.sprint.mission.discodeit.secure.JwtAuthenticationFilter;
import com.sprint.mission.discodeit.secure.handler.JwtLoginSuccessHandler;
import com.sprint.mission.discodeit.secure.handler.LoginFailureHandler;
import com.sprint.mission.discodeit.secure.handler.SpaCsrfTokenRequestHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final LoginFailureHandler loginFailureHandler;
  private final DiscodeitUserDetailsService userDetailsService;
  private final JwtLoginSuccessHandler jwtLoginSuccessHandler;
  private final JwtAuthenticationFilter jwtAuthenticationFilter;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http, SessionRegistry sessionRegistry)
      throws Exception {
    http
        .csrf(csrf -> csrf
            .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
            .csrfTokenRequestHandler(new SpaCsrfTokenRequestHandler())
        )
        .formLogin(login -> login
            .loginProcessingUrl("/api/auth/login")
            .successHandler(jwtLoginSuccessHandler)
            .failureHandler(loginFailureHandler)
            .permitAll()
        )
        .logout(logout -> logout
            .logoutUrl("/api/auth/logout")
            .logoutSuccessHandler(
                new HttpStatusReturningLogoutSuccessHandler(HttpStatus.NO_CONTENT))
            .invalidateHttpSession(true)
            .deleteCookies("JSESSIONID", "remember-me")
            .permitAll()
        )
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
            .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/auth/csrf-token").permitAll()
            .requestMatchers("/actuator/**").permitAll()
            .requestMatchers("/", "/index.html", "/assets/**", "/favicon.ico").permitAll()
            .anyRequest()
            .authenticated()
        )
        .exceptionHandling(ex -> ex
            .authenticationEntryPoint((request, response, e) -> {
              response.setStatus(HttpStatus.UNAUTHORIZED.value());
            })
            .accessDeniedHandler((request, response, e) -> {
              response.setStatus(HttpStatus.FORBIDDEN.value());
            })
        )
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        )
        .rememberMe(remember -> remember
            .rememberMeParameter("remember-me")
            .tokenValiditySeconds(3600)
            .userDetailsService(userDetailsService)
            .key("discodeit-remember-me-key")
            .rememberMeCookieName("remember-me")
            .useSecureCookie(false)
            .alwaysRemember(false)
        )
        .addFilterBefore(
            jwtAuthenticationFilter,
            UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }


  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public RoleHierarchy roleHierarchy() {
    return RoleHierarchyImpl.withDefaultRolePrefix()
        .role("ADMIN").implies("CHANNEL_MANAGER")
        // "ADMIN이 CHANNEL_MANAGER를 포함한다"를 코드로 명확하게 표현
        .role("CHANNEL_MANAGER").implies("USER")
        .build();
  }

  @Bean
  static MethodSecurityExpressionHandler methodSecurityExpressionHandler(
      RoleHierarchy roleHierarchy) {
    DefaultMethodSecurityExpressionHandler handler = new DefaultMethodSecurityExpressionHandler();
    handler.setRoleHierarchy(roleHierarchy);
    return handler;
  }

}
