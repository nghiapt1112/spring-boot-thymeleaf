package com.lyna.commons.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:messages_en.properties")
@ConfigurationProperties
public class messagePropertiesConfig {
    private String mpackage;

    public String getMpackage() {
        return mpackage;
    }

    public void setMpackage(String mpackage) {
        this.mpackage = mpackage;
    }
}
