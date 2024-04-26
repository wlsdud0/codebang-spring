package com.example.project.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    private String resourcePath = "/upload/**"; // view에서 접근할 경로

    // 맥 로컬
//    private String savePath = "file:///Users/jinyoung/springboot_img/"; // 실제 파일 저장 경로

    // 리눅스 서버
    private String savePath = "file:///home/gjwlsdud730/springboot_img/"; // 실제 파일 저장 경로

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(resourcePath).addResourceLocations(savePath);
    }
}
