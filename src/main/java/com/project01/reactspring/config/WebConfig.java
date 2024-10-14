package com.project01.reactspring.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Cấu hình để Spring phục vụ file từ thư mục uploads/avatar/
        registry.addResourceHandler("/avatar/**")
                .addResourceLocations("file:uploads/avatar/");
    }
}