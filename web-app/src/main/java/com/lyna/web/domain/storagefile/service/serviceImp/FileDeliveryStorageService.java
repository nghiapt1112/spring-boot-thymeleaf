package com.lyna.web.domain.storagefile.service.serviceImp;

import com.lyna.commons.infrustructure.exception.DomainException;
import com.lyna.commons.utils.Constants;
import com.lyna.web.domain.delivery.Delivery;
import com.lyna.web.domain.delivery.DeliveryDetail;
import com.lyna.web.domain.delivery.repository.DeliveryDetailRepository;
import com.lyna.web.domain.delivery.repository.DeliveryRepository;
import com.lyna.web.domain.mpackage.Package;
import com.lyna.web.domain.mpackage.repository.PackageRepository;
import com.lyna.web.domain.order.OrderDetail;
import com.lyna.web.domain.order.repository.OrderRepository;
import com.lyna.web.domain.postCourse.repository.PostCourseRepository;
import com.lyna.web.domain.storagefile.exeption.StorageException;
import com.lyna.web.domain.storagefile.service.StorageDeliveryService;
import com.lyna.web.domain.stores.Store;
import com.lyna.web.domain.stores.repository.StoreRepository;
import com.lyna.web.domain.trainningData.service.TrainingService;
import com.lyna.web.domain.user.User;
import com.lyna.web.domain.view.CsvDelivery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.lyna.commons.utils.DataUtils.isNumeric;

@Service
public class FileDeliveryStorageService extends BaseStorageService implements StorageDeliveryService {

    private Map<String, Object> mapStoreCodeCsv;
    private Map<String, Object> mapKeyDelivery;
    private Map<String, Object> mapDeliveryIdCsv;
    private List<String> listStoreCode;
    private List<String> listPost;
    private Set<Store> storeIterable;
    private Set<Delivery> deliveryIterable;
    private Set<DeliveryDetail> deliveryDetailIterable;
    private String READ_FILE_FAILED = "err.csv.readFileFailed.msg";
    private Map<String, Map<String, BigDecimal>> mapOrderPackageIdAmount;
    private Map<String, String> setStoreCodePost;
    private Map<String, String> mapStoreCodeStoreId;
    private Map<String, String> mapDeliveryIdOrderId;
    private Map<String, Map<String, BigDecimal>> mapOrderIdProductIdAmount;

    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private PackageRepository packageRepository;
    @Autowired
    private DeliveryDetailRepository deliveryDetailRepository;
    @Autowired
    private PostCourseRepository postCourseRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private TrainingService trainingService;
    @Autowired
    private DeliveryRepository deliveryRepository;

    @Override
    public Map<Integer, String> store(User user, MultipartFile file, String typeUploadFile) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        int tenantId = user.getTenantId();
        String userId = user.getId();
        if (file.isEmpty()) {
            setMapError(500, toStr("err.csv.fileEmpty.msg") + filename);
        }

        if (filename.contains("..")) {
            setMapError(501, toStr("err.csv.seeOtherPath.msg"));
        }

        try (InputStream inputStream = file.getInputStream()) {
            Reader reader = new InputStreamReader(inputStream);
            innitDataGeneral();
            initDataDelivery();
            Iterator<CsvDelivery> deliveryIterator = deliveryRepository.getMapDelivery(reader);
            validateDataUploadDelivery(deliveryIterator);
            if (getSizeMapError() == 0) {
                setMapDataDelivery(tenantId, userId, typeUploadFile);
                if (!storeIterable.isEmpty() || !checkExistsPostCoursesIterable() || !deliveryIterable.isEmpty() || !deliveryDetailIterable.isEmpty()) {
                    saveDataDelivery();
                }

                if (mapOrderIdProductIdAmount.size() > 0 && mapOrderPackageIdAmount.size() > 0)
                    trainingService.saveMap(mapOrderIdProductIdAmount, mapOrderPackageIdAmount, tenantId, userId);
            }
        } catch (Exception ex) {
            setMapError(502, toStr(READ_FILE_FAILED));
        }
        return getMapError();
    }

    private void validateDataUploadDelivery(Iterator<CsvDelivery> deliveryIterator) {
        HashSet<String> setDelivery = new HashSet<>();

        while (deliveryIterator.hasNext()) {
            CsvDelivery csvDelivery = deliveryIterator.next();
            int row = 1;

            if (csvDelivery.getPost() == null || csvDelivery.getPost().isEmpty()) {
                setMapError(01 + row, "行目 " + row + " にポストが不正");
            }

            if (csvDelivery.getStoreCode() == null || csvDelivery.getStoreCode().isEmpty()) {
                setMapError(02 + row, "行目 " + row + " に店舗コードが不正");
            }

            if (csvDelivery.getBox() == null
                    || !isNumeric(csvDelivery.getBox())
                    || csvDelivery.getCaseP() == null
                    || !isNumeric(csvDelivery.getCaseP())
                    || csvDelivery.getTray() == null
                    || !isNumeric(csvDelivery.getTray())) {
                setMapError(03 + row, "行目 " + row + " にデータが不正");
            }

            row++;

            setDelivery = setDataWithCsvDelivery(csvDelivery, setDelivery);

        }
    }

    private HashSet<String> setDataWithCsvDelivery(CsvDelivery csvDelivery, HashSet<String> setDelivery) {
        String keyOrder = csvDelivery.getStoreCode() + "#" + csvDelivery.getPost() + "#" + csvDelivery.getOrderDate();
        String skeyCheck = keyOrder.trim().toLowerCase() + "#" + csvDelivery.getOrderDate().trim().toLowerCase();
        if (!setDelivery.contains(skeyCheck)) {
            setDelivery.add(skeyCheck);
            mapStoreCodeCsv.put(csvDelivery.getStoreCode().toLowerCase().trim(), csvDelivery);
            mapKeyDelivery.put(skeyCheck, csvDelivery);
            listStoreCode.add(csvDelivery.getStoreCode().trim().toLowerCase());
            listPost.add(csvDelivery.getPost().trim().toLowerCase());
        }

        return setDelivery;
    }

    void initDataDelivery() {
        setStoreCodePost = new HashMap<>();
        mapKeyDelivery = new HashMap<>();
        mapDeliveryIdCsv = new HashMap<>();
        deliveryIterable = new HashSet<>();
        deliveryDetailIterable = new HashSet<>();
        listStoreCode = new ArrayList<>();
        listPost = new ArrayList<>();
        mapStoreCodeCsv = new HashMap<>();
        storeIterable = new HashSet<>();
        mapStoreCodeStoreId = new HashMap<>();
        mapDeliveryIdOrderId = new HashMap<>();
        mapOrderPackageIdAmount = new HashMap<>();
        mapOrderIdProductIdAmount = new HashMap<>();
    }

    private void setMapDataDelivery(int tenantId, String userId, String typeUploadFile) throws StorageException {
        List<String> result = storeRepository.getAllByCodesAndTenantId(tenantId, listStoreCode);
        List<Store> storesInDb = storeRepository.getAll(tenantId, listStoreCode);

        setDataMapStoreCodeWithStoreInDb(result, storesInDb);

        setDataMapStoreWithNotInDb(tenantId, userId);

        mapKeyDelivery.forEach((storeCodeOrderDate, csvDelivery) -> {
            String storeCode = ((CsvDelivery) csvDelivery).getStoreCode();
            String post = ((CsvDelivery) csvDelivery).getPost();
            String keyStoreCodePost = storeCode + "#" + post;
            if (!setStoreCodePost.containsKey(keyStoreCodePost)) {
                String storeId = mapStoreCodeStoreId.get(storeCode.trim());
                setMapStorePostCourse(tenantId, csvDelivery, post, keyStoreCodePost, storeId, userId);
            } else {
                setMapCsvPostCourse(csvDelivery, setStoreCodePost.get(keyStoreCodePost));
            }
        });

        setMapDeliveryIdOrderId(tenantId, userId);

        if (mapOrderIdProductIdAmount == null || mapOrderIdProductIdAmount.size() == 0) {
            setMapError(504, "発注データが存在しない。");
        }

        setMapOrderPackageIdAmount(tenantId, userId, typeUploadFile);
    }

    private Map<String, String> setDataMapStoreCodeWithStoreInDb(List<String> result, List<Store> storesInDb) {
        List<String> stores = result.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toList());

        listStoreCode.removeIf(x -> stores.contains(x.trim().toLowerCase()));
        storesInDb.forEach(store -> {
            mapStoreCodeStoreId.put(store.getCode(), store.getStoreId());
        });


        return mapStoreCodeStoreId;
    }

    private void setDataMapStoreWithNotInDb(int tenantId, String userId) {
        listStoreCode.forEach(code -> {
            CsvDelivery csvDelivery = (CsvDelivery) mapStoreCodeCsv.get(code);
            Store store = new Store();
            store.setCode(code);
            if (csvDelivery.getStoreName() != null) {
                store.setName(csvDelivery.getStoreName());
            } else
                store.setName(csvDelivery.getStoreCode());
            store.setTenantId(tenantId);
            store.setCreateUser(userId);
            store.setCreateDate(new Date());
            storeIterable.add(store);

            mapStoreCodeStoreId.put(store.getCode().trim(), store.getStoreId());
        });
    }

    private void setMapDeliveryIdOrderId(int tenantId, String userId) {
        mapCsvPostCourseId().forEach((csv, postCourseId) -> {
            List<OrderDetail> orderDetails = orderRepository.getMapByPostCourseIdOrderDateTenantId(postCourseId, ((CsvDelivery) csv).getOrderDate(), tenantId);
            if (orderDetails != null && orderDetails.size() > 0) {

                Map<String, BigDecimal> mapProductAmount = orderDetails.stream().parallel().collect(Collectors.toMap(p -> p.productId, p -> p.getAmount()));
                String orderId = orderDetails.get(0).getOrderId();

                mapOrderIdProductIdAmount.put(orderId, mapProductAmount);
                String deliveryId = deliveryRepository.checkExistByOrderIdAndOrderDate(orderId, ((CsvDelivery) csv).getOrderDate());

                if (deliveryId != null) {
                    mapDeliveryIdCsv.put(deliveryId, csv);
                    mapDeliveryIdOrderId.put(deliveryId, orderId);
                } else {
                    Delivery delivery = new Delivery();
                    delivery.setOrderId(orderId);
                    delivery.setTenantId(tenantId);
                    delivery.setCreateDate(new Date());
                    delivery.setCreateUser(userId);
                    mapDeliveryIdCsv.put(delivery.getDeliveryId(), csv);
                    deliveryIterable.add(delivery);
                    mapDeliveryIdOrderId.put(delivery.getDeliveryId(), orderId);
                }
            }
        });
    }

    private void setMapOrderPackageIdAmount(int tenantId, String userId, String typeUploadFile) {

        List<Package> mapPackage = packageRepository.findByTenantId(tenantId);

        mapDeliveryIdCsv.forEach((deliveryId, csv) -> {
            String trayAmount = ((CsvDelivery) csv).getTray();
            String trayCase = ((CsvDelivery) csv).getCaseP();
            String trayBox = ((CsvDelivery) csv).getBox();
            Map<String, BigDecimal> setPackageIdAmount = new HashMap<>();
            String orderId = mapDeliveryIdOrderId.get(deliveryId);

            mapPackage.forEach(aPackage -> {
                String packageName = aPackage.getName();
                String packageId = aPackage.getPackageId();
                DeliveryDetail deliveryDetailDb = deliveryDetailRepository.checkExistByDeliveryId(deliveryId, packageId, tenantId);

                if (deliveryDetailDb == null) {
                    BigDecimal amount = getAmountPackage(trayAmount, trayCase, trayBox, packageName);
                    if (amount.compareTo(BigDecimal.ZERO) > 0) {
                        DeliveryDetail deliveryDetail = new DeliveryDetail();
                        deliveryDetail.setDeliveryId(deliveryId);
                        deliveryDetail.setTenantId(tenantId);
                        deliveryDetail.setPackageId(packageId);
                        deliveryDetail.setAmount(amount);
                        deliveryDetail.setCreateDate(new Date());
                        deliveryDetail.setCreateUser(userId);
                        setPackageIdAmount.put(packageId, amount);
                        deliveryDetailIterable.add(deliveryDetail);
                    }
                } else {
                    BigDecimal amount = getAmountPackage(trayAmount, trayCase, trayBox, packageName);
                    if (typeUploadFile.equals(Constants.UPDATE_DATA)) {
                        deliveryDetailDb.setAmount(amount);
                        deliveryDetailIterable.add(deliveryDetailDb);
                    }
                    setPackageIdAmount.put(packageId, amount);
                }
            });

            mapOrderPackageIdAmount.put(orderId, setPackageIdAmount);
        });
    }

    private BigDecimal getAmountPackage(String trayAmount, String trayCase, String trayBox, String packageName) {
        BigDecimal amount = new BigDecimal(0);
        if (packageName.equals("ばんじゅう")) {
            amount = new BigDecimal(trayAmount);
        } else if (packageName.equals("箱")) {
            amount = new BigDecimal(trayBox);
        } else if (packageName.equals("ケース"))
            amount = new BigDecimal(trayCase);

        return amount;
    }

    void saveDataMaster() throws DomainException {
        //save all storeCode
        storeRepository.saveAll(storeIterable);
        //save all postcourse
        postCourseRepository.saveAll(getPostCoursesIterable());
    }

    @Transactional
    void saveDataDelivery() throws DomainException {
        saveDataMaster();
        //Save delivery
        deliveryRepository.saveAll(deliveryIterable);
        //save delivery detail
        deliveryDetailRepository.saveAll(deliveryDetailIterable);
    }
}
