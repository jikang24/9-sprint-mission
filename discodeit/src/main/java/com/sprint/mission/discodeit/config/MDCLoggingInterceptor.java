package com.sprint.mission.discodeit.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;
import org.slf4j.MDC;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;

public class MDCLoggingInterceptor implements HandlerInterceptor {

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
      Object handler) {
    String requestId = UUID.randomUUID().toString();

    MDC.put("requestId", requestId);
    MDC.put("method", request.getMethod());
    MDC.put("url", request.getRequestURI());

    response.setHeader("Discodeit-Request-ID", requestId);

    return true;
  }


  // 응답 완료 후 (예외 발생해도 실행)
  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
      Object handler, @Nullable Exception ex) {
    MDC.clear();
  }

}
