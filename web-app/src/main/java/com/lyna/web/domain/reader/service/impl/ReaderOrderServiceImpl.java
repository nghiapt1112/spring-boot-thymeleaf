package com.lyna.web.domain.reader.service.impl;

import com.lyna.web.domain.reader.service.ReaderOrderService;
import com.lyna.web.domain.storagefile.StorageProperties;
import com.lyna.web.domain.storagefile.service.serviceImp.BaseStorageService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

@Service
public class ReaderOrderServiceImpl extends BaseStorageService implements ReaderOrderService {
    public ReaderOrderServiceImpl(StorageProperties properties) {
        super(properties);
    }

    @Override
    public Set<Iterator<String>> getMapData(MultipartFile file) {
        Set<Iterator<String>> iterators = new HashSet<>();

        try {
            Workbook workbook = new XSSFWorkbook(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            int i = 0;
            for (Row row : sheet) {
                if (i > 0) {
                    ArrayList<String> list = new ArrayList<>();
                    for (Cell cell : row) {
                        list.add(String.valueOf(cell));

                    }
                    iterators.add(list.iterator());
                }
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return iterators;
    }
}
