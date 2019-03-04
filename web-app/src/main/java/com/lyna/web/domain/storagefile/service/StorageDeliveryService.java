package com.lyna.web.domain.storagefile.service;

import com.lyna.web.domain.user.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface StorageDeliveryService {
    Map<Integer, String> store(User user, MultipartFile file, String typeUploadFile);

    int getExtension(MultipartFile file);
}
