package com.zlhhh.networksecurity.config;

import com.zlhhh.networksecurity.config.interceptor.JwtInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor())
                .addPathPatterns("/**")  // 拦截所有请求，通过判断token是否合法来决定是否需要登录
                .excludePathPatterns("/**/login", "/**/register", "/area/**", "/entity/**")  // 放行的接口
                .excludePathPatterns("/swagger-ui/**", "/swagger-resources/**", "/v3/**", "/error/**");  // 放行swagger
        WebMvcConfigurer.super.addInterceptors(registry);
    }
    @Bean
    public JwtInterceptor jwtInterceptor() {
        return new JwtInterceptor();
    }
}
