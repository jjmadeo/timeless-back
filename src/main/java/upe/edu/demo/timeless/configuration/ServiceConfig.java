package upe.edu.demo.timeless.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import upe.edu.demo.timeless.shared.interceptor.RequestInterceptor;

public class ServiceConfig implements WebMvcConfigurer {

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(requestInterceptor());
  }

  @Bean
  public RequestInterceptor requestInterceptor() {
    return new RequestInterceptor();
  }
}
