package com.lyna.web.domain.storagefile.service.serviceImp;

import com.lyna.commons.utils.Constants;
import com.lyna.web.domain.delivery.repository.DeliveryRepository;
import com.lyna.web.domain.reader.service.impl.BaseStorageDeliveryService;
import com.lyna.web.domain.storagefile.StorageProperties;
import com.lyna.web.domain.storagefile.service.StorageDeliveryService;
import com.lyna.web.domain.user.User;
import com.lyna.web.domain.view.CsvDelivery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

@Service
public class FileDeliveryStorageService extends BaseStorageDeliveryService implements StorageDeliveryService {

    @Autowired
    private DeliveryRepository deliveryRepository;

    public FileDeliveryStorageService(StorageProperties properties) {
        super(properties);
    }

    @Override
    public int getExtension(MultipartFile file) {
        return getExtensionFile(file);
    }


    @Override
    public Map<Integer, String> store(User user, MultipartFile file, String typeUploadFile) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        int tenantId = user.getTenantId();
        String userId = user.getId();

        innitDataGeneral();
        initDataDelivery();

        if (file.isEmpty()) {
            mapError.put(500, toStr("err.csv.fileEmpty.msg") + filename);
        }

        if (filename.contains("..")) {
            mapError.put(501, toStr("err.csv.seeOtherPath.msg"));
        }

        try (InputStream inputStream = file.getInputStream()) {
            Reader reader = new InputStreamReader(inputStream);
            Iterator<CsvDelivery> deliveryIterator = deliveryRepository.getMapDelivery(reader);
            validateDataUploadDelivery(deliveryIterator);
            saveDataDeliveryAndWithSaveTrainingData(tenantId, userId, typeUploadFile);
        } catch (Exception ex) {
            mapError.put(502, toStr(READ_FILE_FAILED));
        }
        return mapError;
    }

    private void validateDataUploadDelivery(Iterator<CsvDelivery> deliveryIterator) {
        HashSet<String> setDelivery = new HashSet<>();

        while (deliveryIterator.hasNext()) {
            CsvDelivery csvDelivery = deliveryIterator.next();
            int row = 1;
            validateData(row, csvDelivery);
            row++;

            setDelivery = setDataWithCsvDelivery(csvDelivery, setDelivery);
        }
    }


}
