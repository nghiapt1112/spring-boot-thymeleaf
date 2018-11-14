package com.lyna.web;

import com.lyna.web.domain.user.service.impl.UserServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.lyna.commons", "com.lyna.web", "com.lyna.security"})
public class LynaApplication {

    @Bean
    public UserServiceImpl userService() {
        return new UserServiceImpl();
    }

    public static void main(String[] args) {
        SpringApplication.run(LynaApplication.class, args);
    }
}
