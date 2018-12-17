package com.lyna.web.domain.storagefile.service;

import com.lyna.web.domain.user.User;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public interface StorageService {
    void init();

    List<String> store(User user, MultipartFile file, int type);

    Stream<Path> loadAll();

    Path load(String filename);

    Resource loadAsResource(String filename);

    void deleteAll();
}
