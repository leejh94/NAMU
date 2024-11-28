package com.namu.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    @Value("${cors.allowed.origins}")
    private String allowedOrigins;

    @Bean
    public WebMvcConfigurer webConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins(allowedOrigins)
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        .allowCredentials(true);
            }

            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
                registry.addViewController("/{path1:[a-zA-Z0-9-]+}")
                        .setViewName("forward:/index.html");
                registry.addViewController("/{path1:[a-zA-Z0-9-]+}/{path2:[a-zA-Z0-9-]+}")
                        .setViewName("forward:/index.html");
                registry.addViewController("/error").setViewName("forward:/index.html");
                // `/auth/login`으로 시작하는 모든 경로를 index.html로
                registry.addViewController("/auth/login/**")
                        .setViewName("forward:/index.html");
            }

            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                registry.addResourceHandler("/**")
                        .addResourceLocations("classpath:/static/");
                registry.addResourceHandler("/static/**")
                        .addResourceLocations("classpath:/static/");
            }

        };
    }
}
