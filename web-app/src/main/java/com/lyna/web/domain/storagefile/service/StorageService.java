package com.lyna.web.domain.storagefile.service;

import com.lyna.web.domain.storagefile.exeption.StorageException;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Stream;

public interface StorageService {
    void init();

    Set<StorageException> store(int tenantId, MultipartFile file, int type);

    Stream<Path> loadAll();

    Path load(String filename);

    Resource loadAsResource(String filename);

    void deleteAll();
}
