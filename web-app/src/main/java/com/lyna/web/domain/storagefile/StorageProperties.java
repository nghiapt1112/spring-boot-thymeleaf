package com.lyna.web.domain.storagefile;

import org.springframework.boot.context.properties.ConfigurationProperties;

// TODO: create `storage` property in application.yml file.
// TODO: move this file to com.lyna.web.infrastructure.property
@ConfigurationProperties("storage")
public class StorageProperties {

    /**
     * Folder location for storing files
     */
    // TODO: create property in application.yml instead of hard_code
    private String location = "upload-dir";

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
