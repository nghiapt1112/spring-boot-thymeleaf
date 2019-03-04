package com.lyna.web.domain.reader.service.impl;

import com.lyna.commons.infrustructure.exception.DomainException;
import com.lyna.commons.utils.Constants;
import com.lyna.commons.utils.DataUtils;
import com.lyna.web.domain.delivery.Delivery;
import com.lyna.web.domain.delivery.DeliveryDetail;
import com.lyna.web.domain.delivery.repository.DeliveryDetailRepository;
import com.lyna.web.domain.delivery.repository.DeliveryRepository;
import com.lyna.web.domain.mpackage.Package;
import com.lyna.web.domain.mpackage.repository.PackageRepository;
import com.lyna.web.domain.order.OrderDetail;
import com.lyna.web.domain.order.repository.OrderRepository;
import com.lyna.web.domain.postCourse.repository.PostCourseRepository;
import com.lyna.web.domain.reader.ExcelDelivery;
import com.lyna.web.domain.storagefile.StorageProperties;
import com.lyna.web.domain.storagefile.exeption.StorageException;
import com.lyna.web.domain.storagefile.service.serviceImp.BaseStorageService;
import com.lyna.web.domain.stores.Store;
import com.lyna.web.domain.stores.repository.StoreRepository;
import com.lyna.web.domain.trainningData.service.TrainingService;
import com.lyna.web.domain.view.CsvDelivery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public abstract class BaseStorageDeliveryService extends BaseStorageService {

    protected Map<String, Object> mapStoreCodeCsv;
    protected Map<String, Object> mapKeyDelivery;
    protected Map<String, Object> mapDeliveryIdCsv;
    protected List<String> listStoreCode;
    protected List<String> listPost;
    protected Set<Store> storeIterable;
    protected Set<Delivery> deliveryIterable;
    protected Set<DeliveryDetail> deliveryDetailIterable;
    protected String READ_FILE_FAILED = "err.csv.readFileFailed.msg";
    protected Map<String, Map<String, BigDecimal>> mapOrderPackageIdAmount;
    protected Map<String, String> setStoreCodePost;
    protected Map<String, String> mapStoreCodeStoreId;
    protected Map<String, String> mapDeliveryIdOrderId;
    protected Map<String, Map<String, BigDecimal>> mapOrderIdProductIdAmount;

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private PackageRepository packageRepository;
    @Autowired
    private DeliveryDetailRepository deliveryDetailRepository;
    @Autowired
    private PostCourseRepository postCourseRepository;
    @Autowired
    private TrainingService trainingService;
    @Autowired
    private DeliveryRepository deliveryRepository;

    public BaseStorageDeliveryService(StorageProperties properties) {
        super(properties);
    }

    protected void initDataDelivery() {
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

    protected void validateData(int row, Object data) {
        String post = null, storeCode = null;
        String box = null;
        String caseP = null;
        String tray = null;

        if (data instanceof ExcelDelivery) {
            ExcelDelivery excelDelivery = (ExcelDelivery) data;
            post = excelDelivery.getPost();
            storeCode = excelDelivery.getStoreCode();
            box = excelDelivery.getBox();
            caseP = excelDelivery.getCaseP();
            tray = excelDelivery.getTray();
        }
        if (data instanceof CsvDelivery) {
            CsvDelivery csvDelivery = (CsvDelivery) data;
            post = csvDelivery.getPost();
            storeCode = csvDelivery.getStoreCode();
            box = csvDelivery.getBox();
            tray = csvDelivery.getTray();
            caseP = csvDelivery.getCaseP();
        }

        if (post == null || post.isEmpty()) {
            mapError.put(01 + row, "行目 " + row + " にポストが不正");
        }

        if (storeCode == null || storeCode.isEmpty()) {
            mapError.put(02 + row, "行目 " + row + " に店舗コードが不正");
        }

        if (box != null && !DataUtils.isNumeric(box)) {
            mapError.put(03 + row, "行目 " + row + " にデータが不正");
        }

        if (caseP != null && !DataUtils.isNumeric(caseP)) {
            mapError.put(04 + row, "行目 " + row + " にデータが不正");
        }

        if (tray != null && !DataUtils.isNumeric(tray)) {
            mapError.put(05 + row, "行目 " + row + " にデータが不正");
        }
    }

    protected HashSet<String> setDataWithCsvDelivery(Object objectData, HashSet<String> setDelivery) {
        String storeCode = "", post = "", orderDate = "";

        if (objectData instanceof ExcelDelivery) {
            ExcelDelivery deliveryExcel = (ExcelDelivery) objectData;
            storeCode = deliveryExcel.getStoreCode().trim().toLowerCase();
            post = deliveryExcel.getPost().trim().toLowerCase();
            orderDate = deliveryExcel.getOrderDate().trim().toLowerCase();
        } else if (objectData instanceof CsvDelivery) {
            CsvDelivery deliveryExcel = (CsvDelivery) objectData;
            storeCode = deliveryExcel.getStoreCode().trim().toLowerCase();
            post = deliveryExcel.getPost().trim().toLowerCase();
            orderDate = deliveryExcel.getOrderDate().trim().toLowerCase();
        }

        String keyDelivery = storeCode + "#" + post + "#" + orderDate;
        if (!setDelivery.contains(keyDelivery)) {
            setDelivery.add(keyDelivery);
            mapStoreCodeCsv.put(storeCode, objectData);
            mapKeyDelivery.put(keyDelivery, objectData);
            listStoreCode.add(storeCode);
            listPost.add(post);
        }

        return setDelivery;
    }

    protected void saveDataDeliveryAndWithSaveTrainingData(int tenantId, String userId, String typeUploadFile) {
        if (mapError.size() == 0) {
            try {
                setMapDataDelivery(tenantId, userId, typeUploadFile);
                if (!storeIterable.isEmpty() || !postCoursesIterable.isEmpty() || !deliveryIterable.isEmpty() || !deliveryDetailIterable.isEmpty()) {
                    saveDataDelivery();
                }

                if (mapOrderIdProductIdAmount.size() > 0 && mapOrderPackageIdAmount.size() > 0)
                    trainingService.saveMap(mapOrderIdProductIdAmount, mapOrderPackageIdAmount, tenantId, userId);
            }catch (Exception ex){
                throw new StorageException(" CSVファイルの保存に失敗しました。");
            }
        }
    }

    private void setMapDataDelivery(int tenantId, String userId, String typeUploadFile) throws StorageException {
        List<String> result = storeRepository.getAllByCodesAndTenantId(tenantId, listStoreCode);
        List<Store> storesInDb = storeRepository.getAll(tenantId, listStoreCode);

        setDataMapStoreCodeWithStoreInDb(result, storesInDb);

        setDataMapStoreWithNotInDb(tenantId, userId);

        mapKeyDelivery.forEach((storeCodeOrderDate, dataDelivery) -> {
            String storeCode = getStoreCode(dataDelivery);
            String post = getPost(dataDelivery);
            String keyStoreCodePost = storeCode + "#" + post;
            if (!setStoreCodePost.containsKey(keyStoreCodePost)) {
                String storeId = mapStoreCodeStoreId.get(storeCode.trim());
                setMapStorePostCourse(tenantId, dataDelivery, post, keyStoreCodePost, storeId, userId);
            } else {
                mapCsvPostCourseId.put(dataDelivery, setStoreCodePost.get(keyStoreCodePost));
            }
        });

        setMapDeliveryIdOrderId(tenantId, userId);

        if (mapOrderIdProductIdAmount == null || mapOrderIdProductIdAmount.size() == 0) {
            mapError.put(504, "発注データが存在しない。");
        }

        setMapOrderPackageIdAmount(tenantId, userId, typeUploadFile);
    }

    private String getStoreCode(Object dataDelivery) {
        if (dataDelivery instanceof CsvDelivery) {
            return ((CsvDelivery) dataDelivery).getStoreCode();
        } else if (dataDelivery instanceof ExcelDelivery) {
            return ((ExcelDelivery) dataDelivery).getStoreCode();
        }
        return "";
    }

    private String getStoreName(Object dataDelivery) {
        if (dataDelivery instanceof CsvDelivery) {
            return ((CsvDelivery) dataDelivery).getStoreName();
        } else if (dataDelivery instanceof ExcelDelivery) {
            return ((ExcelDelivery) dataDelivery).getStoreName();
        }
        return "";
    }

    private String getPost(Object dataDelivery) {
        if (dataDelivery instanceof CsvDelivery) {
            return ((CsvDelivery) dataDelivery).getPost();
        } else if (dataDelivery instanceof ExcelDelivery) {
            return ((ExcelDelivery) dataDelivery).getPost();
        }
        return "";
    }

    private String getOrderDate(Object dataDelivery) {
        if (dataDelivery instanceof CsvDelivery) {
            return ((CsvDelivery) dataDelivery).getOrderDate();
        } else if (dataDelivery instanceof ExcelDelivery) {
            return ((ExcelDelivery) dataDelivery).getOrderDate();
        }
        return "";
    }

    private String getTrayAmount(Object dataDelivery) {
        if (dataDelivery instanceof CsvDelivery) {
            return ((CsvDelivery) dataDelivery).getTray();
        } else if (dataDelivery instanceof ExcelDelivery) {
            return ((ExcelDelivery) dataDelivery).getTray();
        }
        return "";
    }

    private String getCaseAmount(Object dataDelivery) {
        if (dataDelivery instanceof CsvDelivery) {
            return ((CsvDelivery) dataDelivery).getCaseP();
        } else if (dataDelivery instanceof ExcelDelivery) {
            return ((ExcelDelivery) dataDelivery).getCaseP();
        }
        return "";
    }

    private String getBoxAmount(Object dataDelivery) {
        if (dataDelivery instanceof CsvDelivery) {
            return ((CsvDelivery) dataDelivery).getBox();
        } else if (dataDelivery instanceof ExcelDelivery) {
            return ((ExcelDelivery) dataDelivery).getBox();
        }
        return "";
    }

    private String getCardBoardAmount(Object dataDelivery) {
        if (dataDelivery instanceof CsvDelivery) {
            return ((CsvDelivery) dataDelivery).getCardBox();
        } else if (dataDelivery instanceof ExcelDelivery) {
            return ((ExcelDelivery) dataDelivery).getCardBox();
        }
        return "";
    }

    private Map<String, String> setDataMapStoreCodeWithStoreInDb(List<String> result, List<Store> storesInDb) {
        List<String> stores = result.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toList());

        listStoreCode.removeIf(x -> stores.contains(x.trim().toLowerCase()));
        storesInDb.forEach(store -> mapStoreCodeStoreId.put(store.getCode(), store.getStoreId()));

        return mapStoreCodeStoreId;
    }

    private void setDataMapStoreWithNotInDb(int tenantId, String userId) {
        listStoreCode.forEach(code -> {
            Object dataDelivery = mapStoreCodeCsv.get(code);
            String storeName = getStoreName(dataDelivery);

            Store store = new Store();
            store.setCode(code);
            if (storeName != null) {
                store.setName(storeName);
            } else
                store.setName(code);
            store.setTenantId(tenantId);
            store.setCreateUser(userId);
            store.setCreateDate(new Date());
            storeIterable.add(store);

            mapStoreCodeStoreId.put(store.getCode().trim(), store.getStoreId());
        });
    }

    private void setMapDeliveryIdOrderId(int tenantId, String userId) {
        mapCsvPostCourseId.forEach((csv, postCourseId) -> {
            String orderDate = getOrderDate(csv);

            List<OrderDetail> orderDetails = orderRepository.getMapByPostCourseIdOrderDateTenantId(postCourseId, orderDate, tenantId);
            if (orderDetails != null && orderDetails.size() > 0) {

                Map<String, BigDecimal> mapProductAmount = orderDetails.stream().parallel().collect(Collectors.toMap(p -> p.productId, p -> p.getAmount()));
                String orderId = orderDetails.get(0).getOrderId();

                mapOrderIdProductIdAmount.put(orderId, mapProductAmount);
                String deliveryId = deliveryRepository.checkExistByOrderIdAndOrderDate(orderId, orderDate);

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

        mapDeliveryIdCsv.forEach((deliveryId, csvData) -> {
            String trayAmount = getTrayAmount(csvData);
            String trayCase = getCaseAmount(csvData);
            String trayBox = getBoxAmount(csvData);
            String cardBoard = getCardBoardAmount(csvData);

            Map<String, BigDecimal> setPackageIdAmount = new HashMap<>();
            String orderId = mapDeliveryIdOrderId.get(deliveryId);

            mapPackage.forEach(aPackage -> {
                String packageName = aPackage.getName();
                String packageId = aPackage.getPackageId();
                DeliveryDetail deliveryDetailDb = deliveryDetailRepository.checkExistByDeliveryId(deliveryId, packageId, tenantId);

                if (deliveryDetailDb == null) {
                    BigDecimal amount = getAmountPackage(trayAmount, trayCase, trayBox, cardBoard, packageName);
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
                    BigDecimal amount = getAmountPackage(trayAmount, trayCase, trayBox, cardBoard, packageName);
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

    private BigDecimal getAmountPackage(String trayAmount, String trayCase, String trayBox, String cardBoard, String packageName) {
        BigDecimal amount = new BigDecimal(0);
        if (trayAmount != null && packageName.equals("ばんじゅう")) {
            amount = new BigDecimal(trayAmount);
        } else if (trayBox != null && packageName.equals("箱")) {
            amount = new BigDecimal(trayBox);
        } else if (trayCase != null && packageName.equals("ケース"))
            amount = new BigDecimal(trayCase);
        else if (cardBoard != null && packageName.equals("段ボール"))
            amount = new BigDecimal(cardBoard);

        return amount;
    }

    @Transactional
    void saveDataDelivery() throws DomainException {
        saveDataMaster();
        //Save delivery
        deliveryRepository.saveAll(deliveryIterable);
        //save delivery detail
        deliveryDetailRepository.saveAll(deliveryDetailIterable);
    }

    void saveDataMaster() throws DomainException {
        //save all storeCode
        storeRepository.saveAll(storeIterable);
        //save all postcourse
        postCourseRepository.saveAll(postCoursesIterable);
    }
}