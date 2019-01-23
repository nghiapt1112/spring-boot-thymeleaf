package com.lyna.web.domain.storagefile.service;

import com.lyna.web.domain.user.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface UploadDataService {
    void init();

    Map<Integer, String> store(User tenantId, MultipartFile file, int type, String typeUploadFile);
}
