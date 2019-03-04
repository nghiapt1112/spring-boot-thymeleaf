package com.lyna.web.domain.storagefile.service;

import com.lyna.web.domain.user.User;
import org.apache.commons.csv.CSVRecord;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public interface StorageService {
    void init();

    String storeFileServer(MultipartFile file);

    Map<Integer, String> checkDataAndCreateOrderWithGetDataAI(User user, String fileName, InputStream inputStream, String typeUploadFile, Map<Integer, String> mapHeader);

    Stream<Path> loadAll();

    Path load(String filename);

    Resource loadAsResource(String filename);

    Resource loadAsFileResource(String filename);

    void deleteAll();

    Map<String, Integer> getMapHeader(MultipartFile file, int extensionFile);

    List<CSVRecord> getMapData(MultipartFile file);

    Map<Integer, String> getMapHeader();

    int getExtension(MultipartFile file);
}
