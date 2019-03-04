package com.lyna.web.domain.reader.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Iterator;
import java.util.Set;

public interface ReaderOrderService {
    Set<Iterator<String>> getMapData(MultipartFile file);
}
