package com.lyna.lyna;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.nghia.libraries.commons.mss", "com.lyna.lyna", "com.lyna.security"})
public class LynaApplication {

    public static void main(String[] args) {
        SpringApplication.run(LynaApplication.class, args);
    }
}
