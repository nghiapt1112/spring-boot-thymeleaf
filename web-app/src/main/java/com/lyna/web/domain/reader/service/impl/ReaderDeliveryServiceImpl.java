package com.lyna.web.domain.reader.service.impl;

import com.lyna.commons.infrustructure.exception.DomainValidateExeption;
import com.lyna.commons.utils.Constants;
import com.lyna.web.domain.reader.ExcelDelivery;
import com.lyna.web.domain.reader.service.ReaderDeliveryService;
import com.lyna.web.domain.storagefile.StorageProperties;
import com.lyna.web.domain.user.User;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Repository
public class ReaderDeliveryServiceImpl extends BaseStorageDeliveryService implements ReaderDeliveryService {

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

    public ReaderDeliveryServiceImpl(StorageProperties properties) {
        super(properties);
    }

    @Override
    public List<ExcelDelivery> readFileDelivery(MultipartFile file) throws IOException {
        List<ExcelDelivery> delivers;

        if (file.isEmpty()) {
            mapError.put(500, toStr("err.csv.fileEmpty.msg"));
        }

        delivers = readExcelDelivery(file);

        return delivers;
    }

    public List<ExcelDelivery> readExcelDelivery(MultipartFile file) throws IOException {
        List<ExcelDelivery> delivers = new ArrayList<>();

        try {
            Workbook workbook = new XSSFWorkbook(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            int i = 0;
            for (Row row : sheet) {
                if (i > 0) {
                    ExcelDelivery delivery = new ExcelDelivery();
                    for (Cell cell : row) {
                        if (cell.getColumnIndex() == Constants.FILE_DELIVERY.ORDER_DATE)
                            delivery.setOrderDate(getDataCell(cell));
                        if (cell.getColumnIndex() == Constants.FILE_DELIVERY.STORE_CODE)
                            delivery.setStoreCode(getDataCell(cell));
                        if (cell.getColumnIndex() == Constants.FILE_DELIVERY.STORE_NAME)
                            delivery.setStoreName(getDataCell(cell));
                        if (cell.getColumnIndex() == Constants.FILE_DELIVERY.POST)
                            delivery.setPost(getDataCell(cell));
                        if (cell.getColumnIndex() == Constants.FILE_DELIVERY.TRAY)
                            delivery.setTray(getDataCell(cell));
                        if (cell.getColumnIndex() == Constants.FILE_DELIVERY.BOX)
                            delivery.setBox(getDataCell(cell));
                        if (cell.getColumnIndex() == Constants.FILE_DELIVERY.CARD_BOX)
                            delivery.setCardBox(getDataCell(cell));
                        if (cell.getColumnIndex() == Constants.FILE_DELIVERY.CASE)
                            delivery.setCaseP(getDataCell(cell));
                    }
                    delivers.add(delivery);
                }
                i++;
            }
        } catch (Exception ex) {
            throw new DomainValidateExeption(" CSVファイルを読み取れない。");
        }
        return delivers;
    }

    private String getDataCell(Cell cell) {
        switch (cell.getCellTypeEnum()) {
            case STRING:
                return cell.getRichStringCellValue().getString();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return sdf.format(cell.getDateCellValue());
                } else {
                    return ((XSSFCell) cell).getRawValue() + "";
                }
            case BOOLEAN:
                return cell.getBooleanCellValue() + "";
            case FORMULA:
                return cell.getCellFormula() + "";
        }
        return "";
    }


    public void validateDataAndSetMap(List<ExcelDelivery> delivers) {
        AtomicReference<HashSet<String>> setDelivery = new AtomicReference<>(new HashSet<>());

        delivers.forEach(excelDelivery -> {
            int row = 1;
            validateData(row, excelDelivery);
            row++;
            setDelivery.set(setDataWithCsvDelivery(excelDelivery, setDelivery.get()));
        });
    }

    @Override
    public Map<Integer, String> readAndSaveDataDelivery(User user, MultipartFile file, String typeUploadFile) {
        try {
            int tenantId = user.getTenantId();
            String userId = user.getId();

            innitDataGeneral();
            initDataDelivery();

            List<ExcelDelivery> deliveryIterator = readExcelDelivery(file);

            if (deliveryIterator.size() > 0) {
                validateDataAndSetMap(deliveryIterator);

                saveDataDeliveryAndWithSaveTrainingData(tenantId, userId, typeUploadFile);
            } else {
                mapError.put(502, "発注データが存在しない。");
            }
        } catch (Exception ex) {
            mapError.put(502, ex.getMessage());
        }
        return mapError;
    }
}
