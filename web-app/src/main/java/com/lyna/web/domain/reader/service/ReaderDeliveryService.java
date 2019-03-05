package com.lyna.web.domain.reader.service;

import com.lyna.web.domain.reader.ExcelDelivery;
import com.lyna.web.domain.user.User;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ReaderDeliveryService {
    List<ExcelDelivery> readFileDelivery(MultipartFile file) throws IOException, SAXException, InvalidFormatException;

    Map<Integer, String> readAndSaveDataDelivery(User user, MultipartFile file, String typeUploadFile);
}
