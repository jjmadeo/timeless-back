package upe.edu.demo.timeless.shared.interceptor;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Slf4j
public class RequestInterceptor implements HandlerInterceptor {

  private static final String NO_PARAM = "no hay parametro";
  private static final String NO_HEADER = "no hay header";
  private static final String AUTHORIZATION = "Authorization";

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    log.info("RequestInterceptor.preHandle URI {}", request.getRequestURI());
    log.info(
        "RequestInterceptor.preHandle PARAMETRO {}",
        Optional.of(request.getQueryString()).orElse(NO_PARAM));
    log.info(
        "RequestInterceptor.preHandle TOKEN {}",
        Optional.of(request.getHeader(AUTHORIZATION)).orElse(NO_HEADER));

    return HandlerInterceptor.super.preHandle(request, response, handler);
  }

  @Override
  public void postHandle(
      HttpServletRequest request,
      HttpServletResponse response,
      Object handler,
      ModelAndView modelAndView)
      throws Exception {
    log.info("RequestInterceptor.postHandle URI {}", request.getRequestURI());
    log.info(
        "RequestInterceptor.postHandle PARAMETRO {}",
        Optional.of(request.getQueryString()).orElse(NO_PARAM));
    log.info(
        "RequestInterceptor.postHandle TOKEN {}",
        Optional.of(request.getHeader(AUTHORIZATION)).orElse(NO_HEADER));
    HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
  }

  @Override
  public void afterCompletion(
      HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
      throws Exception {
    log.info("RequestInterceptor.afterCompletion URI {}", request.getRequestURI());
    log.info(
        "RequestInterceptor.afterCompletion PARAMETRO {}",
        Optional.of(request.getQueryString()).orElse(NO_PARAM));
    log.info(
        "RequestInterceptor.afterCompletion TOKEN {}",
        Optional.of(request.getHeader(AUTHORIZATION)).orElse(NO_HEADER));
    HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
  }
}
