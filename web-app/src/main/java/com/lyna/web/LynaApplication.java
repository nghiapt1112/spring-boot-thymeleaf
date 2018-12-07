package com.lyna.web;

import com.lyna.web.domain.storagefile.StorageProperties;
import com.lyna.web.domain.storagefile.service.StorageService;
import com.lyna.web.domain.user.service.impl.UserServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.lyna.commons", "com.lyna.web"})
@EnableConfigurationProperties(StorageProperties.class)
public class LynaApplication {

    public static void main(String[] args) {
        SpringApplication.run(LynaApplication.class, args);
    }

    @Bean
    public UserServiceImpl userService() {
        return new UserServiceImpl();
    }

    @Bean
    CommandLineRunner init(StorageService storageService) {
        return (args) -> {
            storageService.deleteAll();
            storageService.init();
        };
    }

}
