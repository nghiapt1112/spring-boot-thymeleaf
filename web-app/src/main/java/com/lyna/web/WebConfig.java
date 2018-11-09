package com.lyna.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(
                "/webjars/**",
                "/images/**",
                "/js/**",
                "/css/**"
        ).addResourceLocations(
                "classpath:/META-INF/resources/webjars/",
                "classpath:/static/images/",
                "classpath:/static/js/",
                "classpath:/static/css/"
        );
    }

}
