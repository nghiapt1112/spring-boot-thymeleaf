package com.lyna.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.lyna.commons", "com.lyna.web", "com.lyna.security"})
public class LynaApplication {

    public static void main(String[] args) {
        SpringApplication.run(LynaApplication.class, args);
    }
}
