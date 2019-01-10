package com.lyna.web.domain.storagefile.service.serviceImp;

import com.lyna.commons.infrustructure.exception.DomainException;
import com.lyna.commons.infrustructure.service.BaseService;
import com.lyna.web.domain.AI.AIService;
import com.lyna.web.domain.delivery.Delivery;
import com.lyna.web.domain.delivery.DeliveryDetail;
import com.lyna.web.domain.delivery.repository.DeliveryDetailRepository;
import com.lyna.web.domain.delivery.repository.DeliveryRepository;
import com.lyna.web.domain.mpackage.Package;
import com.lyna.web.domain.mpackage.repository.PackageRepository;
import com.lyna.web.domain.order.Order;
import com.lyna.web.domain.order.OrderDetail;
import com.lyna.web.domain.order.repository.OrderDetailRepository;
import com.lyna.web.domain.order.repository.OrderRepository;
import com.lyna.web.domain.postCourse.PostCourse;
import com.lyna.web.domain.postCourse.repository.PostCourseRepository;
import com.lyna.web.domain.product.Product;
import com.lyna.web.domain.product.repository.ProductRepository;
import com.lyna.web.domain.storagefile.StorageProperties;
import com.lyna.web.domain.storagefile.exeption.StorageException;
import com.lyna.web.domain.storagefile.service.StorageService;
import com.lyna.web.domain.stores.Store;
import com.lyna.web.domain.stores.repository.StoreRepository;
import com.lyna.web.domain.user.User;
import com.lyna.web.domain.view.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.lyna.commons.utils.DataUtils.isNumeric;
import static com.lyna.commons.utils.DateTimeUtils.converStringToDate;

@Service
public class FileSystemStorageService extends BaseService implements StorageService {

    private final Path rootLocation;

    List<String> listStoreCode;
    List<String> listProductCode;
    List<String> listPackageName;
    List<String> ListPost;
    Map<String, CsvOrder> mapProduct;
    Map<String, CsvOrder> mapProductOrderCsv;
    Map<String, Object> mapStorePostCode;
    Map<String, Object> mapStore;
    //TODO: change Iterable to closest type of these collection. Ex: Collection, Map, Set, List.
    Set<Product> productIterable;
    Set<Store> storeIterable;
    Set<Package> packageIterable;
    Iterable<PostCourse> postCoursesIterable;
    Iterable<Delivery> deliveryIterable;
    Iterable<DeliveryDetail> deliveryDetailIterable;
    Set<Order> orderIterable;
    Set<OrderDetail> orderDetailIterable;

    Map<Object, String> mapCsvPostCourseId;
    Map<String, Object> mapDeliveryIdCsv;
    Map<String, CsvOrder> mapProductIdCsvOrder;
    Map<Integer, String> mapError;

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private PostCourseRepository postCourseRepository;
    @Autowired
    private DeliveryRepository deliveryRepository;
    @Autowired
    private DeliveryDetailRepository deliveryDetailRepository;
    @Autowired
    private PackageRepository packageRepository;

    @Autowired
    private AIService aiService;

    @Autowired
    public FileSystemStorageService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    @Override
    @Transactional
    public Map<Integer, String> store(User user, MultipartFile file, int type) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        int tenantId = user.getTenantId();
        String userId = user.getId();
        initData();
        if (file.isEmpty()) {
            mapError.put(500, "空のファイルを保存出来ない。" + filename);
        }

        if (filename.contains("..")) {
            mapError.put(500, "現在のディレクトリの外に相対パスを持つファイルを保存できません");
        }

        try (InputStream inputStream = file.getInputStream()) {
            Reader reader = new InputStreamReader(inputStream);
            if (type == 1) {
                innitDataOrder();
                Iterator<CsvOrder> orderIterator = orderRepository.getMapOrder(reader);
                processUpload(orderIterator);

                setMapData(tenantId, userId);
                setDataOrder(tenantId, userId);

                saveDataMaster();
                saveDataOrder();
                aiService.calculateLogisticsWithAI(user, orderDetailIterable.stream().map(OrderDetail::getOrderId).collect(Collectors.toSet()));
            }
//            else if (type == 3) {
//                innitDataStore();
//                if (mapError.size() == 0) {
//                    Iterator<CsvStore> storeIterator = storeRepository.getMapStore(reader);
//                    processUploadStore(storeIterator);
//                }
//                setDataStore(user);
//            } else if (type == 4) {
//                innitDataProduct();
//                if (mapError.size() == 0) {
//                    Iterator<CsvProduct> productIterator = productRepository.getMapStore(reader);
//                    processUploadProduct(productIterator);
//                }
//                setDataProduct(user);
//            } else if (type == 5) {
//                innitDataPackage();
//                if (mapError.size() == 0) {
//                    Iterator<CsvPackage> packageIterator = packageRepository.getMapStore(reader);
//                    processUploadPackage(packageIterator);
//                }
//                setDataPackage(user);
//            }
            else {
                innitDataDelivery();
                Iterator<CsvDelivery> deliveryIterator = deliveryRepository.getMapDelivery(reader);
                processUploadDelivery(deliveryIterator);
                if (mapError.size() == 0) {
                    setMapDataDelivery(tenantId, userId);
                    saveDataMaster();
                    saveDataDelivery();
                }
            }
        } catch (Exception ex) {
            mapError.put(500, "ファイル保存に失敗しました。");
        }
        return mapError;
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        } catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }
    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageException("Could not read file: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new StorageException("保存されたファイルの読み込みは失敗した " + filename, e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }

    void initData() {
        listStoreCode = new ArrayList<>();
        ListPost = new ArrayList<>();
        mapStorePostCode = new HashMap<>();
        mapStore = new HashMap<>();
        storeIterable = new HashSet<>();
        postCoursesIterable = new HashSet<>();
        mapCsvPostCourseId = new HashMap<>();
        mapError = new HashMap<>();
    }


    void innitDataOrder() {
        listProductCode = new ArrayList<>();
        mapProduct = new HashMap<>();
        mapProductOrderCsv = new HashMap<>();
        productIterable = new HashSet<>();
        orderIterable = new HashSet<>();
        orderDetailIterable = new HashSet<>();
        mapProductIdCsvOrder = new HashMap<>();
    }

    void innitDataDelivery() {
        mapDeliveryIdCsv = new HashMap<>();
        deliveryIterable = new HashSet<>();
        deliveryDetailIterable = new HashSet<>();
    }

//    void innitDataStore() {
//        mapData = new HashMap<>();
//        storeIterable = new HashSet<>();
//        listStoreCode = new ArrayList<>();
//    }
//
//    void innitDataProduct() {
//        mapData = new HashMap<>();
//        productIterable = new HashSet<>();
//        listProductCode = new ArrayList<>();
//    }
//
//    void innitDataPackage() {
//        mapData = new HashMap<>();
//        packageIterable = new HashSet<>();
//        listPackageName = new ArrayList<>();
//    }

    private String getProductIdForKey(String productIdOrderDate) {
        String[] sProductIdOrderDate = productIdOrderDate.split("_");
        return sProductIdOrderDate[0];
    }

//    private void processUploadPackage(Iterator<CsvPackage> packageIterator) {
//        while (packageIterator.hasNext()) {
//            CsvPackage csvPackage = packageIterator.next();
//            int row = 1;
//            if (csvPackage.getPackageName() == null
//                    || csvPackage.getPackageName().isEmpty()
//                    || csvPackage.getUnit() == null
//                    || csvPackage.getUnit().isEmpty()
//                    || csvPackage.getEmptyWeight() == null
//                    || csvPackage.getFullLoadWeight() == null
//                    || csvPackage.getEmptyCapacity() == null
//                    || csvPackage.getFullLoadCapacity() == null
//
//            ) {
//                mapError.put(500, "行目 " + row + " にデータが不正");
//            }
//            row++;
//
//            mapData.put(csvPackage.getPackageName().toLowerCase().trim(), csvPackage);
//            listPackageName.add(csvPackage.getPackageName().toLowerCase().trim());
//
//        }
//    }
//
//    private void processUploadProduct(Iterator<CsvProduct> productIterator) {
//        while (productIterator.hasNext()) {
//            CsvProduct csvProduct = productIterator.next();
//            int row = 1;
//            if (csvProduct.getProductCode() == null
//                    || csvProduct.getProductCode().isEmpty()
//                    || csvProduct.getProductName() == null
//                    || csvProduct.getProductName().isEmpty()
//                    || csvProduct.getUnit() == null
//                    || csvProduct.getUnit().isEmpty()
//                    || csvProduct.getUnitPrice() == null
//                    || csvProduct.getCategory1() == null
//                    || csvProduct.getCategory1().isEmpty()
//                    || csvProduct.getCategory2() == null
//                    || csvProduct.getCategory2().isEmpty()
//                    || csvProduct.getCategory3() == null
//                    || csvProduct.getCategory3().isEmpty()
//
//            ) {
//                mapError.put(500, "行目 " + row + " にデータが不正");
//            }
//            row++;
//
//            mapData.put(csvProduct.getProductCode().trim(), csvProduct);
//            listProductCode.add(csvProduct.getProductCode());
//
//        }
//    }
//
//    private void processUploadStore(Iterator<CsvStore> storeIterator) {
//        while (storeIterator.hasNext()) {
//            CsvStore csvStore = storeIterator.next();
//            int row = 1;
//            if (csvStore.getPost() == null
//                    || csvStore.getPost().isEmpty()
//                    || csvStore.getStoreName().isEmpty()
//                    || csvStore.getStoreName() == null
//                    || csvStore.getPersonInCharge().isEmpty()
//                    || csvStore.getPersonInCharge() == null
//                    || csvStore.getPhoneNumber() == null
//                    || csvStore.getPhoneNumber().isEmpty()
//                    || csvStore.getAddress() == null
//                    || csvStore.getAddress().isEmpty()
//                    || csvStore.getArea() == null
//                    || csvStore.getMajorArea().isEmpty()
//                    || csvStore.getStoreCode() == null
//                    || csvStore.getStoreCode().isEmpty()
//
//            ) {
//                mapError.put(500, "行目 " + row + " にデータが不正");
//            }
//            row++;
//
//            mapData.put(csvStore.getStoreCode().toLowerCase().trim(), csvStore);
//            listStoreCode.add(csvStore.getStoreCode());
//
//        }
//    }

    private void processUpload(Iterator<CsvOrder> orderIterator) {
        HashSet<String> setOrder = new HashSet<>();
        while (orderIterator.hasNext()) {
            CsvOrder csvOrder = orderIterator.next();
            int row = 1;

            if (csvOrder.getPost() == null
                    || csvOrder.getPost().isEmpty()
                    || csvOrder.getOrderDate() == null
                    || csvOrder.getOrderDate().isEmpty()
                    || converStringToDate(csvOrder.getOrderDate().trim().toLowerCase()) == null
                    || csvOrder.getQuantity() == null
                    || csvOrder.getQuantity().isEmpty()
                    || csvOrder.getStoreCode() == null
                    || csvOrder.getStoreCode().isEmpty()
                    || csvOrder.getProductCode() == null
                    || csvOrder.getProductCode().isEmpty()
                    || !isNumeric(csvOrder.getQuantity())
            ) {
                mapError.put(500, "行目 " + row + " にデータが不正");
            }

            row++;

            String keyOrder = csvOrder.getStoreCode().trim().toLowerCase() + "_" + csvOrder.getPost().trim().toLowerCase() + "_" + csvOrder.getOrderDate();
            String skeyOrderCheck = keyOrder + "_" + csvOrder.getProductCode().trim().toLowerCase();
            if (!setOrder.contains(skeyOrderCheck)) {
                setOrder.add(skeyOrderCheck);
                csvOrder.setPost(csvOrder.getPost().toLowerCase().trim());
                csvOrder.setProductCode(csvOrder.getProductCode().toLowerCase().trim());
                csvOrder.setStoreCode(csvOrder.getStoreCode().toLowerCase().trim());
                mapStore.put(csvOrder.getStoreCode().toLowerCase().trim(), csvOrder);
                mapStorePostCode.put(skeyOrderCheck, csvOrder);
                listStoreCode.add(csvOrder.getStoreCode().trim().toLowerCase());
                listProductCode.add(csvOrder.getProductCode().trim().toLowerCase());
                ListPost.add(csvOrder.getPost().trim().toLowerCase());
                mapProduct.put(csvOrder.getProductCode().trim().toLowerCase(), csvOrder);
                mapProductOrderCsv.put(csvOrder.getProductCode().trim().toLowerCase() + "_" + csvOrder.getOrderDate().trim(), csvOrder);
            }
        }
    }

    private void processUploadDelivery(Iterator<CsvDelivery> deliveryIterator) {
        HashSet<String> setDelivery = new HashSet<>();

        while (deliveryIterator.hasNext()) {
            CsvDelivery csvDelivery = deliveryIterator.next();
            int row = 1;
            if (csvDelivery.getPost() == null
                    || csvDelivery.getPost().isEmpty()
                    || csvDelivery.getStoreCode() == null
                    || csvDelivery.getStoreCode().isEmpty()
                    || csvDelivery.getStoreName() == null
                    || csvDelivery.getStoreName().isEmpty()
                    || csvDelivery.getBox() == null
                    || !isNumeric(csvDelivery.getBox())
                    || csvDelivery.getCaseP() == null
                    || !isNumeric(csvDelivery.getCaseP())
                    || csvDelivery.getTray() == null
                    || !isNumeric(csvDelivery.getTray())) {
                mapError.put(500, "行目 " + row + " にデータが不正");
            }
            row += 1;
            String keyOrder = csvDelivery.getStoreCode() + "_" + csvDelivery.getPost() + "_" + csvDelivery.getOrderDate();
            String skeyCheck = keyOrder.trim().toLowerCase() + "_" + csvDelivery.getOrderDate().trim().toLowerCase();
            if (!setDelivery.contains(skeyCheck)) {
                setDelivery.add(skeyCheck);
                mapStore.put(csvDelivery.getStoreCode().toLowerCase().trim(), csvDelivery);
                mapStorePostCode.put(skeyCheck, csvDelivery);
                listStoreCode.add(csvDelivery.getStoreCode().trim().toLowerCase());
                ListPost.add(csvDelivery.getPost().trim().toLowerCase());
            }
        }
    }

    private void setMapDataDelivery(int tenantId, String userId) throws StorageException {
        List<String> result = storeRepository.getAllByCodesAndTenantId(tenantId, listStoreCode);
        List<Store> storesInDb = storeRepository.getAll(tenantId, listStoreCode);
        Map<String, String> mapStoreCodeStoreId = new HashMap<>();
        Map<String, String> setStoreCodePost = new HashMap<>();

        List<String> stores = result.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toList());

        listStoreCode.removeIf(x -> stores.contains(x.trim().toLowerCase()));
        storesInDb.forEach(store -> {
            mapStoreCodeStoreId.put(store.getCode(), store.getStoreId());
        });

        listStoreCode.forEach(code -> {
            CsvDelivery csvDelivery = (CsvDelivery) mapStore.get(code);
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

        mapStorePostCode.forEach((storeCodeOrderDate, csvDelivery) -> {
            String storeCode = getProductIdForKey(storeCodeOrderDate);
            String post = ((CsvDelivery) csvDelivery).getPost();
            String skey = storeCode + "_" + post;
            if (!setStoreCodePost.containsKey(skey)) {
                String storeId = mapStoreCodeStoreId.get(storeCode.trim());
                setMapStorePostCourse(tenantId, setStoreCodePost, csvDelivery, post, skey, storeId, userId);
            } else {
                mapCsvPostCourseId.put(csvDelivery, setStoreCodePost.get(skey));
            }

        });

        mapCsvPostCourseId.forEach((csv, postcodesId) -> {
            String orderId = orderRepository.checkExists(postcodesId, ((CsvDelivery) csv).getOrderDate(), tenantId);
            if (orderId != null) {
                String deliveryId = deliveryRepository.checkExistByOrderIdAndOrderDate(orderId, ((CsvDelivery) csv).getOrderDate());
                if (deliveryId != null)
                    mapDeliveryIdCsv.put(deliveryId, csv);
                else {
                    Delivery delivery = new Delivery();
                    delivery.setOrderId(orderId);
                    delivery.setTenantId(tenantId);
                    delivery.setCreateDate(new Date());
                    delivery.setCreateUser(userId);
                    mapDeliveryIdCsv.put(delivery.getDeliveryId(), csv);
                    ((HashSet<Delivery>) deliveryIterable).add(delivery);
                }
            } else {
                mapError.put(500, "発注データが存在しない。");
            }
        });

        List<Package> mapPackage = packageRepository.findByTenantId(tenantId);

        mapDeliveryIdCsv.forEach((deliveryId, csv) -> {
            String trayAmount = ((CsvDelivery) csv).getTray();
            String trayCase = ((CsvDelivery) csv).getCaseP();
            String trayBox = ((CsvDelivery) csv).getBox();

            mapPackage.forEach(aPackage -> {
                String packageName = aPackage.getName();
                String packageId = aPackage.getPackageId();

                String deliveryDetailId = deliveryDetailRepository.checkExistByDeliveryId(deliveryId, packageId, tenantId);
                if (deliveryDetailId == null) {
                    BigDecimal amount = new BigDecimal(0);
                    if (packageName.equals("ばんじゅう")) {
                        amount = new BigDecimal(trayAmount);
                    } else if (packageName.equals("箱")) {
                        amount = new BigDecimal(trayBox);
                    } else if (packageName.equals("ケース"))
                        amount = new BigDecimal(trayCase);

                    if (amount.compareTo(BigDecimal.ZERO) > 0) {
                        DeliveryDetail deliveryDetail = new DeliveryDetail();
                        deliveryDetail.setDeliveryId(deliveryId);
                        deliveryDetail.setTenantId(tenantId);
                        deliveryDetail.setPackageId(packageId);
                        deliveryDetail.setAmount(amount);
                        deliveryDetail.setCreateDate(new Date());
                        deliveryDetail.setCreateUser(userId);
                        ((HashSet<DeliveryDetail>) deliveryDetailIterable).add(deliveryDetail);
                    }
                }
            });
        });
    }

    private void setMapStorePostCourse(int tenantId, Map<String, String> setStoreCodePost, Object csvDelivery, String post, String skey, String storeId, String userId) {
        String postCourseId = postCourseRepository.checkByStoreIdAndPost(storeId, post);
        postCourseId = getGetPostCourseId(tenantId, storeId, post, postCourseId, userId);
        setStoreCodePost.put(skey, postCourseId);
        mapCsvPostCourseId.put(csvDelivery, postCourseId);
    }

    private String getGetPostCourseId(int tenantId, String storeId, String post, String postCourseId, String userId) {
        if (postCourseId == null) {
            PostCourse postCourse = new PostCourse();
            postCourse.setPost(post);
            postCourse.setStoreId(storeId);
            postCourse.setTenantId(tenantId);
            postCourse.setCreateDate(new Date());
            postCourse.setCreateUser(userId);
            postCourseId = postCourse.getPostCourseId();
            ((HashSet<PostCourse>) postCoursesIterable).add(postCourse);
        }
        return postCourseId;
    }

    private void setMapData(int tenantId, String userId) throws StorageException {
        try {
            List<String> result = storeRepository.getAllByCodesAndTenantId(tenantId, listStoreCode);
            List<Store> storesInDb = storeRepository.getAll(tenantId, listStoreCode);
            List<String> resultProducts = productRepository.getListProductCodeByProductCode(tenantId, listProductCode);
            List<Product> productInDB = productRepository.getProductsByProductCode(tenantId, listProductCode);
            Map<String, String> mapProductCodeProductId = new HashMap<>();
            Map<String, String> mapStoreCodeStoreId = new HashMap<>();
            Set<String> setStoreCode = new HashSet<>();
            Set<String> setProductCode = new HashSet<>();
            Map<String, String> setStoreCodePost = new HashMap<>();

            List<String> stores = result.stream()
                    .map(String::toLowerCase)
                    .collect(Collectors.toList());

            List<String> products = resultProducts.stream()
                    .map(String::toLowerCase)
                    .collect(Collectors.toList());

            listProductCode.removeIf(x -> products.contains(x));

            listStoreCode.removeIf(x -> stores.contains(x));

            storesInDb.forEach(store -> {
                mapStoreCodeStoreId.put(store.getCode(), store.getStoreId());
            });

            listStoreCode.forEach(code -> {
                if (!setStoreCode.contains(code)) {
                    setStoreCode.add(code);
                    CsvOrder csvOrder = (CsvOrder) mapStore.get(code);
                    Store store = new Store();
                    store.setCode(code);
                    store.setName(csvOrder.getStoreName());
                    store.setTenantId(tenantId);
                    store.setCreateDate(new Date());
                    store.setCreateUser(userId);
                    storeIterable.add(store);
                    mapStoreCodeStoreId.put(store.getCode(), store.getStoreId());
                }
            });

            mapStorePostCode.forEach((storeCodeOrderDate, csvOrder) -> {
                String post = ((CsvOrder) csvOrder).getPost();
                String storeCode = getProductIdForKey(storeCodeOrderDate);
                String skey = storeCode + "_" + post;
                if (!setStoreCodePost.containsKey(skey)) {
                    String storeId = mapStoreCodeStoreId.get(storeCode);
                    setMapStorePostCourse(tenantId, setStoreCodePost, csvOrder, post, skey, storeId, userId);
                } else {
                    mapCsvPostCourseId.put(csvOrder, setStoreCodePost.get(skey));
                }
            });

            listProductCode.forEach(code -> {
                if (!setProductCode.contains(code)) {
                    setProductCode.add(code);
                    CsvOrder csvOrder = mapProduct.get(code);
                    Product product = new Product();
                    product.setCode(code);
                    product.setName(csvOrder.getProductName());
                    product.setCategory1(csvOrder.getCategory1());
                    product.setCategory2(csvOrder.getCategory2());
                    product.setCategory3(csvOrder.getCategory3());
                    product.setTenantId(tenantId);
                    product.setCreateUser(userId);
                    product.setCreateDate(new Date());
                    product.setUnit("");
                    product.setPrice(new BigDecimal(0));
                    productIterable.add(product);
                    mapProductCodeProductId.put(code, product.getProductId());
                }
            });

            productInDB.forEach(product -> {
                String productCode = product.getCode();
                mapProductCodeProductId.put(productCode, product.getProductId());
            });

            mapProductOrderCsv.forEach((sProductCodeOderDate, csvOrder) -> {
                String productCode = getProductIdForKey(sProductCodeOderDate);
                String productId = mapProductCodeProductId.get(productCode);
                mapProductIdCsvOrder.put(productId + "_" + csvOrder.getOrderDate(), csvOrder);
            });
        } catch (Exception ex) {
            mapError.put(500, "ファイル保存に失敗しました。");
        }
    }

    private void setDataOrder(int tenantId, String userId) {
        if (mapError.size() == 0) {
            Map<String, String> mapKeyOrderId = new HashMap<>();

            mapProductIdCsvOrder.forEach((sProductIdOrderDate, csvOrder) -> {
                String postCourseId = mapCsvPostCourseId.get(csvOrder);
                String productId = getProductIdForKey(sProductIdOrderDate);
                String orderId = "";
                String key = postCourseId + "_" + csvOrder.getOrderDate().trim();

                orderId = orderRepository.checkExists(postCourseId, csvOrder.getOrderDate().trim(), tenantId);

                if (orderId != null) {
                    List<OrderDetail> orderDetails = orderDetailRepository.findByOrderIdAndProductIdAndTenantId(orderId, productId, tenantId);
                    if (orderDetails != null) {
                        orderDetails.forEach(orderDetail -> {
                            orderDetail.setUpdateDate(new Date());
                            orderDetail.setCreateUser(userId);
                            orderDetail.setAmount(new BigDecimal(csvOrder.getQuantity()));
                            orderDetailIterable.add(orderDetail);
                        });
                    } else
                        setOrderDetailIterable(orderId, productId, csvOrder, tenantId, userId);
                } else {
                    if (mapKeyOrderId != null && !mapKeyOrderId.isEmpty() && mapKeyOrderId.containsKey(key)) {
                        orderId = mapKeyOrderId.get(key);
                    } else {
                        Order order = new Order();
                        orderId = order.getOrderId();
                        mapKeyOrderId.put(key, orderId);
                        Date date = converStringToDate(csvOrder.getOrderDate());
                        if (date == null)
                            date = new Date();

                        order.setOrderDate(date);
                        order.setCreateDate(date);
                        order.setCreateUser(userId);

                        order.setPostCourseId(postCourseId);
                        order.setTenantId(tenantId);
                        orderIterable.add(order);
                    }
                    setOrderDetailIterable(orderId, productId, csvOrder, tenantId, userId);
                }
            });
        }
    }

    private void setOrderDetailIterable(String orderId, String productId, CsvOrder csvOrder, int tenantId, String userId) {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderId(orderId);
        orderDetail.setProductId(productId);
        BigDecimal quantity = new BigDecimal(csvOrder.getQuantity());
        orderDetail.setAmount(quantity);
        orderDetail.setTenantId(tenantId);
        orderDetail.setCreateDate(new Date());
        orderDetail.setCreateUser(userId);
        orderDetailIterable.add(orderDetail);
    }

//    private void setDataPackage(User currentUser) {
//        listPackageName = listPackageName.stream().distinct().collect(Collectors.toList());
//
//        List<Package> packagesInDb = packageRepository.getAllByNameAndTenantId(listPackageName, currentUser.getTenantId());
//
//        List<String> existedPackageNames = packagesInDb.stream().map(t -> t.getName().toLowerCase()).collect(Collectors.toList());
//
//        List<Package> packages = new ArrayList<>();
//        for (String packageName : listPackageName) {
//            if (!existedPackageNames.contains(packageName)) {
//                CsvPackage csvPackage = (CsvPackage) mapData.get(packageName);
//                packages.add(csvPackage.createPackage(currentUser));
//            }
//        }
//        for (Package p : packagesInDb) {
//            CsvPackage csvPackage = (CsvPackage) mapData.get(p.getName().toLowerCase());
//            if (csvPackage != null) {
//                p.setName(csvPackage.getPackageName());
//                p.setUnit(csvPackage.getUnit());
//                p.setEmptyWeight(csvPackage.getEmptyWeight());
//                p.setFullLoadWeight(csvPackage.getFullLoadWeight());
//                p.setEmptyCapacity(csvPackage.getEmptyCapacity());
//                p.setFullLoadCapacity(csvPackage.getFullLoadCapacity());
//                p.setTenantId(currentUser.getTenantId());
//                p.setUpdateUser(currentUser.getId());
//                p.setUpdateDate(new Date());
//                packageIterable.add(p);
//            }
//        }
//        packageRepository.saveAll(packages);
//    }
//
//
//    private void setDataProduct(User currentUser) {
//        listProductCode = listProductCode.stream().distinct().collect(Collectors.toList());
//
//        List<Product> productsInDb = productRepository.getProductsByProductCode(currentUser.getTenantId(), listProductCode);
//
//        List<String> existedProductCodes = productsInDb.stream().map(t -> t.getCode()).collect(Collectors.toList());
//
//        List<Product> products = new ArrayList<>();
//        for (String productCode : listProductCode) {
//            if (!existedProductCodes.contains(productCode)) {
//                CsvProduct csvProduct = (CsvProduct) mapData.get(productCode);
//                products.add(csvProduct.createProduct(currentUser));
//            }
//        }
//        for (Product product : productsInDb) {
//            CsvProduct csvProduct = (CsvProduct) mapData.get(product.getCode().toLowerCase());
//            if (csvProduct != null) {
//                product.setCode(csvProduct.getProductCode());
//                product.setName(csvProduct.getProductName());
//                product.setUnit(csvProduct.getUnit());
//                product.setPrice(csvProduct.getUnitPrice());
//                product.setOrderDetails(product.getOrderDetails());
//                product.setCategory1(csvProduct.getCategory1());
//                product.setCategory2(csvProduct.getCategory2());
//                product.setCategory3(csvProduct.getCategory3());
//                product.setTenantId(currentUser.getTenantId());
//                product.setUpdateUser(currentUser.getId());
//                product.setUpdateDate(new Date());
//                productIterable.add(product);
//            }
//        }
//        productRepository.saveAll(products);
//    }
//
//
//    private void setDataStore(User currentUser) {
//        listStoreCode = listStoreCode.stream().distinct().collect(Collectors.toList());
//
//        List<Store> storesInDb = storeRepository.getAll(currentUser.getTenantId(), listStoreCode);
//
//        List<String> existedStoreCodes = storesInDb.stream().map(t -> t.getCode()).collect(Collectors.toList());
//
//        List<Store> stores = new ArrayList<>();
//        for (String storeCode : listStoreCode) {
//            if (!existedStoreCodes.contains(storeCode)) {
//                CsvStore csvStore = (CsvStore) mapData.get(storeCode);
//                stores.add(csvStore.createStore(currentUser));
//            }
//        }
//        for (Store store : storesInDb) {
//            CsvStore csvStore = (CsvStore) mapData.get(store.getCode());
//            if (csvStore != null) {
//                store.setName(csvStore.getStoreName());
//                store.setArea(csvStore.getArea());
//                store.setMajorArea(csvStore.getMajorArea());
//                store.setPhoneNumber(csvStore.getPhoneNumber());
//                store.setPersonCharge(csvStore.getPersonInCharge());
//                store.setPostCourses(store.getPostCourses());
//                store.setTenantId(currentUser.getTenantId());
//                store.setUpdateUser(currentUser.getId());
//                store.setUpdateDate(new Date());
//                storeIterable.add(store);
//            }
//        }
//
//        storeRepository.saveAll(stores);
//    }

    private void saveDataMaster() throws DomainException {
        //save all storeCode
        storeRepository.saveAll(storeIterable);
        //save all postcourse
        postCourseRepository.saveAll(postCoursesIterable);
    }

    private void saveDataDelivery() throws DomainException {
        //Save delivery
        deliveryRepository.saveAll(deliveryIterable);
        //save delivery detail
        deliveryDetailRepository.saveAll(deliveryDetailIterable);
    }

    private void saveDataOrder() throws DomainException {
        //Save all productCode
        productRepository.saveAll(productIterable);
        orderRepository.saveAll(orderIterable);
        orderDetailRepository.saveAll(orderDetailIterable);
    }
}