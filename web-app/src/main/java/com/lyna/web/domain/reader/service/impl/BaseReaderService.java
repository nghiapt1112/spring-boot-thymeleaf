package com.lyna.web.domain.reader.service.impl;

import com.lyna.commons.infrustructure.service.BaseService;
import com.lyna.web.domain.reader.ExcelDelivery;
import com.lyna.web.domain.storagefile.StorageProperties;
import com.lyna.web.domain.storagefile.exeption.StorageException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseReaderService extends BaseService {
    public static Logger logger = LoggerFactory.getLogger(BaseReaderService.class);

    public Path rootLocation;

    @Autowired
    public BaseReaderService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    protected BaseReaderService() {
    }

    public String storeFileServer(MultipartFile file) {
        String filename = StringUtils.stripFilenameExtension(file.getOriginalFilename()) + "_" + System.currentTimeMillis() + "." + StringUtils.getFilenameExtension(file.getOriginalFilename());
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {
                // This is a security check
                throw new StorageException(
                        "Cannot store file with relative path outside current directory "
                                + filename);
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, this.rootLocation.resolve(filename),
                        StandardCopyOption.REPLACE_EXISTING);
            }
            return filename;
        } catch (IOException e) {
            throw new StorageException("ファイル" + filename + "を格納するのは失敗しました ", e);
        }
    }


    public Map<Integer, List<String>> readExcel(MultipartFile file) throws IOException {
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);

        Map<Integer, List<String>> data = new HashMap<>();
        int i = 0;
        for (Row row : sheet) {
            data.put(i, new ArrayList<>());
            for (Cell cell : row) {
                switch (cell.getCellTypeEnum()) {
                    case STRING:
                        data.get(new Integer(i)).add(cell.getRichStringCellValue().getString());
                        break;
                    case NUMERIC:
                        if (DateUtil.isCellDateFormatted(cell)) {
                            data.get(i).add(cell.getDateCellValue() + "");
                        } else {
                            data.get(i).add(cell.getNumericCellValue() + "");
                        }
                        break;
                    case BOOLEAN:
                        data.get(i).add(cell.getBooleanCellValue() + "");
                        break;
                    case FORMULA:
                        data.get(i).add(cell.getCellFormula() + "");
                        break;
                    default:
                        data.get(new Integer(i)).add(" ");
                }
            }
            i++;
        }

        return data;
    }
}

