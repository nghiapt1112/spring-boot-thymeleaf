package com.lyna.web.domain.storagefile.service.serviceImp;

import com.lyna.commons.infrustructure.exception.DomainException;
import com.lyna.commons.infrustructure.service.BaseService;
import com.lyna.commons.utils.DataUtils;
import com.lyna.web.domain.delivery.Delivery;
import com.lyna.web.domain.delivery.DeliveryDetail;
import com.lyna.web.domain.delivery.repository.DeliveryDetailRepository;
import com.lyna.web.domain.delivery.repository.DeliveryRepository;
import com.lyna.web.domain.order.Order;
import com.lyna.web.domain.order.OrderDetail;
import com.lyna.web.domain.order.exception.StorageException;
import com.lyna.web.domain.order.exception.StorageFileNotFoundException;
import com.lyna.web.domain.order.repository.OrderDetailRepository;
import com.lyna.web.domain.order.repository.OrderRepository;
import com.lyna.web.domain.postCourse.PostCourse;
import com.lyna.web.domain.postCourse.repository.PostCourseRepository;
import com.lyna.web.domain.product.Product;
import com.lyna.web.domain.product.repository.ProductRepository;
import com.lyna.web.domain.storagefile.StorageProperties;
import com.lyna.web.domain.storagefile.service.StorageService;
import com.lyna.web.domain.stores.Store;
import com.lyna.web.domain.stores.repository.StoreRepository;
import com.lyna.web.domain.view.CsvDelivery;
import com.lyna.web.domain.view.CsvOrder;
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

@Service
public class FileSystemStorageService extends BaseService implements StorageService {

    private final Path rootLocation;
    List<String> listStoreCode;
    List<String> listProductCode;
    List<String> ListPost;
    Map<String, CsvOrder> mapProduct;
    Map<String, CsvOrder> mapProductOrderCsv;
    Map<String, Object> mapStorePostCode;
    Map<String, Object> mapStore;
    Map<String, Object> mapStoreCodeOrderCsv;
    Iterable<Product> productIterable;
    Iterable<Store> storeIterable;
    Iterable<PostCourse> postCoursesIterable;
    Iterable<Delivery> deliveryIterable;
    Iterable<DeliveryDetail> deliveryDetailIterable;

    Iterable<Order> orderIterable;
    Iterable<OrderDetail> orderDetailIterable;

    Map<Object, String> mapCsvPostCourseId;
    Map<String, Object> mapDeliveryIdCsv;
    Map<String, String> mapPostCourseIdProductId;
    Map<String, CsvOrder> mapProductIdCsvOrder;
    List<String> mapError;

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
    public FileSystemStorageService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    @Override
    @Transactional
    public List<String> store(int tenantId, MultipartFile file, int type) throws StorageException {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        initData();
        try {
            if (file.isEmpty()) {
                throw new StorageException("空のファイルを保存出来ない。" + filename); //Failed to storeCode empty file
            }
            if (filename.contains("..")) {
                // This is a security check
                throw new StorageException(
                        "現在のディレクトリの外に相対パスを持つファイルを保存できません。"//Cannot storeCode file with relative path outside current directory
                                + filename);
            }

            try (InputStream inputStream = file.getInputStream()) {
                Reader reader = new InputStreamReader(inputStream);
                Iterator<CsvOrder> orderIterator;
                Iterator<CsvDelivery> deliveryIterator;
                if (type == 1) {
                    orderIterator = orderRepository.getMapOrder(reader);
                    processUpload(orderIterator);
                    setMapData(tenantId);
                    //setDataMapProduct();
                    setDataOrder(tenantId);
                    saveDataMaster();
                    saveDataOrder();
                } else {
                    deliveryIterator = deliveryRepository.getMapDelivery(reader);
                    processUploadDelivery(deliveryIterator);
                    setMapDataDelivery(tenantId);
                    if (mapError.size() == 0) {
                        saveDataMaster();
                    }
                }
            }
        } catch (IOException e) {
            throw new StorageException("ファイル保存に失敗しました。" + filename, e);//Failed to storeCode file
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
                throw new StorageFileNotFoundException(
                        "Could not read file: " + filename);

            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("保存されたファイルの読み込みは失敗した " + filename, e);
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
        listProductCode = new ArrayList<>();
        ListPost = new ArrayList<>();
        mapProduct = new HashMap<>();
        mapProductOrderCsv = new HashMap<>();
        mapStorePostCode = new HashMap<>();
        mapStore = new HashMap<>();
        mapStoreCodeOrderCsv = new HashMap<>();
        productIterable = new HashSet<>();
        storeIterable = new HashSet<>();
        postCoursesIterable = new HashSet<>();

        orderIterable = new HashSet<>();
        orderDetailIterable = new HashSet<>();

        mapDeliveryIdCsv = new HashMap<>();

        mapCsvPostCourseId = new HashMap<>();
        mapPostCourseIdProductId = new HashMap<>();
        mapProductIdCsvOrder = new HashMap<>();
        mapError = new ArrayList<>();
        deliveryIterable = new HashSet<>();
        deliveryDetailIterable = new HashSet<>();
    }

    public void setDataMapProduct() {
        //If Orderdate, Store, Post, Product,quantity exists in db => message
        Map<String, CsvOrder> mapProductIdOrder = new HashMap<>();
        mapProductIdCsvOrder.forEach((productIdOrderDate, csvOrder) -> {
            String productId = getProductIdForKey(productIdOrderDate);
            String postCourseId = mapCsvPostCourseId.get(csvOrder);
            String orderId = orderRepository.checkExists(postCourseId, csvOrder.getOrderDate());
            if (orderId == null) {
                mapProductIdOrder.put(productId, csvOrder);
            }
        });

        mapProductIdCsvOrder.clear();
        mapProductIdCsvOrder = mapProductIdOrder;
    }

    private String getProductIdForKey(String productIdOrderDate) {
        String[] sProductIdOrderDate = productIdOrderDate.split("_");
        return sProductIdOrderDate[0];
    }

    private void processUpload(Iterator<CsvOrder> orderIterator) {
        HashSet<String> setOrder = new HashSet<>();
        while (orderIterator.hasNext()) {

            CsvOrder csvOrder = orderIterator.next();
            int row = 1;

            if (csvOrder.getPost() == null || csvOrder.getPost().isEmpty()) {
                mapError.add("行目 " + row + " にデータが不正");
            }
            if (csvOrder.getOrderDate() == null || csvOrder.getOrderDate().isEmpty()) {
                mapError.add("行目 " + row + " にデータが不正");
            }

            if (DataUtils.converStringToDate(csvOrder.getOrderDate().trim().toLowerCase()) == null) {
                mapError.add("行目 " + row + " にデータが不正");
            }

            if (csvOrder.getQuantity() == null || csvOrder.getQuantity().isEmpty()) {
                mapError.add("行目 " + row + " にデータが不正");
            }
            if (csvOrder.getStoreCode() == null || csvOrder.getStoreCode().isEmpty()) {
                mapError.add("行目 " + row + " にデータが不正");
            }
            if (csvOrder.getProductCode() == null || csvOrder.getProductCode().isEmpty()) {
                mapError.add("行目 " + row + " にデータが不正");
            }
            if (csvOrder.getQuantity() == null || !DataUtils.isNumeric(csvOrder.getQuantity())) {
                mapError.add("数量は数字データではない");
            }

            row++;

            String keyOrder = csvOrder.getStoreCode().trim().toLowerCase() + "_" + csvOrder.getPost().trim().toLowerCase() + "_" + csvOrder.getOrderDate();
            String skeyCheck = keyOrder + "_" + csvOrder.getProductCode().trim().toLowerCase();
            if (!setOrder.contains(skeyCheck)) {
                setOrder.add(skeyCheck);

                csvOrder.setPost(csvOrder.getPost().toLowerCase().trim());
                csvOrder.setProductCode(csvOrder.getProductCode().toLowerCase().trim());
                csvOrder.setStoreCode(csvOrder.getStoreCode().toLowerCase().trim());
                mapStore.put(csvOrder.getStoreCode().toLowerCase().trim(), csvOrder);
                mapStorePostCode.put(keyOrder, csvOrder);
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

            if (csvDelivery.getPost() == null || csvDelivery.getPost().isEmpty()) {
                mapError.add("行目 " + row + " にデータが不正");
            }
            if (csvDelivery.getStoreCode() == null || csvDelivery.getStoreCode().isEmpty()) {
                mapError.add("行目 " + row + " にデータが不正");
            }
            if (csvDelivery.getStoreName() == null || csvDelivery.getStoreName().isEmpty()) {
                mapError.add("行目 " + row + " にデータが不正");
            }
            if (csvDelivery.getBox() == null || !DataUtils.isNumeric(csvDelivery.getBox())) {
                mapError.add("数量は数字データではない");
            }
            if (csvDelivery.getCaseP() == null || !DataUtils.isNumeric(csvDelivery.getCaseP())) {
                mapError.add("数量は数字データではない");
            }

            if (csvDelivery.getTray() == null || !DataUtils.isNumeric(csvDelivery.getTray())) {
                mapError.add("数量は数字データではない");
            }

            row++;

            String keyOrder = csvDelivery.getStoreCode() + "_" + csvDelivery.getPost() + "_" + csvDelivery.getOrderDate();
            String skeyCheck = keyOrder.trim().toLowerCase() + "_" + csvDelivery.getOrderDate().trim().toLowerCase();
            if (!setDelivery.contains(skeyCheck)) {
                setDelivery.add(skeyCheck);
                mapStore.put(csvDelivery.getStoreCode().toLowerCase().trim(), csvDelivery);
                mapStorePostCode.put(keyOrder, csvDelivery);
                listStoreCode.add(csvDelivery.getStoreCode().trim().toLowerCase());
                ListPost.add(csvDelivery.getPost().trim().toLowerCase());
            }
        }
    }

    private void setMapDataDelivery(int tenantId) throws StorageException {
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
            ((HashSet<Store>) storeIterable).add(store);

            mapStoreCodeStoreId.put(store.getCode().trim(), store.getStoreId());
        });

        mapStorePostCode.forEach((storeCodeOrderDate, csvDelivery) -> {
            String storeCode = getProductIdForKey(storeCodeOrderDate);
            String post = ((CsvDelivery) csvDelivery).getPost();
            String skey = storeCode + "_" + post;
            if (!setStoreCodePost.containsKey(skey)) {
                String storeId = mapStoreCodeStoreId.get(storeCode.trim());
                String postCourseId = postCourseRepository.checkByStoreIdAndPost(storeId, post);
                postCourseId = getGetPostCourseId(tenantId, storeId, post, postCourseId);
                setStoreCodePost.put(skey, postCourseId);
                mapCsvPostCourseId.put(csvDelivery, postCourseId);
            } else {
                mapCsvPostCourseId.put(csvDelivery, setStoreCodePost.get(skey));
            }
        });


        mapCsvPostCourseId.forEach((csv, postcodesId) -> {
            String orderId = orderRepository.checkExists(postcodesId, ((CsvDelivery) csv).getOrderDate());
            if (orderId != null) {
                String deliveryId = deliveryRepository.checkExistByOrderIdAndOrderDate(orderId, ((CsvDelivery) csv).getOrderDate());
                if (deliveryId != null)
                    mapDeliveryIdCsv.put(deliveryId, csv);
                else {
                    Delivery delivery = new Delivery();
                    delivery.setOrderId(orderId);
                    delivery.setTenantId(tenantId);
                    delivery.setCreateDate(new Date());
                    delivery.setCreateUser("");
                    delivery.setUpdateDate(new Date());
                    delivery.setUpdateUser("");
                    mapDeliveryIdCsv.put(delivery.getDeliveryId(), csv);
                    ((HashSet<Delivery>) deliveryIterable).add(delivery);
                }
            } else {
                mapError.add("発注データが存在しない。");
            }
        });

        Map<String, String> mapPackage = new HashMap<>();

        mapPackage.put("507f191e666c19119de222bb", "ばんじゅう");
        mapPackage.put("507f191e666c19119de222bc", "箱");
        mapPackage.put("507f191e666c19119de222be", "ケース");

        mapDeliveryIdCsv.forEach((deliveryId, csv) -> {
            String trayAmount = ((CsvDelivery) csv).getTray();
            String trayCase = ((CsvDelivery) csv).getCaseP();
            String trayBox = ((CsvDelivery) csv).getBox();
            mapPackage.forEach((packageId, packageName) -> {
                String deliveryDetailId = deliveryDetailRepository.checkExistByDeliveryId(deliveryId, packageId, tenantId);
                if (deliveryDetailId == null) {
                    BigDecimal amount = new BigDecimal(0);
                    if (packageName.equals("ばんじゅう")) {
                        amount = new BigDecimal(trayAmount);
                    } else if (packageName.equals("箱")) {
                        amount = new BigDecimal(trayBox);
                    } else
                        amount = new BigDecimal(trayCase);

                    DeliveryDetail deliveryDetail = new DeliveryDetail();
                    deliveryDetail.setDeliveryId(deliveryId);
                    deliveryDetail.setTenantId(tenantId);
                    deliveryDetail.setPackageId(packageId);
                    deliveryDetail.setAmount(amount);
                    deliveryDetail.setCreateDate(new Date());
                    deliveryDetail.setCreateUser("");
                    deliveryDetail.setUpdateDate(new Date());
                    deliveryDetail.setUpdateUser("");
                    ((HashSet<DeliveryDetail>) deliveryDetailIterable).add(deliveryDetail);
                }
            });
        });
    }

    private String getGetPostCourseId(int tenantId, String storeId, String post, String postCourseId) {
        if (postCourseId == null) {
            PostCourse postCourse = new PostCourse();
            postCourse.setPost(post);
            postCourse.setStoreId(storeId);
            postCourse.setTenantId(tenantId);
            postCourseId = postCourse.getPostCourseId();
            ((HashSet<PostCourse>) postCoursesIterable).add(postCourse);
        }
        return postCourseId;
    }

    private void setMapData(int tenantId) throws StorageException {
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
            //Todo:check exists code

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
                    ((HashSet<Store>) storeIterable).add(store);
                    mapStoreCodeStoreId.put(store.getCode(), store.getStoreId());
                }
            });

            mapStorePostCode.forEach((storeCodeOrderDate, csvOrder) -> {
                String post = ((CsvOrder) csvOrder).getPost();
                String storeCode = getProductIdForKey(storeCodeOrderDate);
                String skey = storeCode + "_" + post;
                if (!setStoreCodePost.containsKey(skey)) {
                    String storeId = mapStoreCodeStoreId.get(storeCode);
                    String postCourseId = postCourseRepository.checkByStoreIdAndPost(storeId, post);
                    postCourseId = getGetPostCourseId(tenantId, storeId, post, postCourseId);
                    setStoreCodePost.put(skey, postCourseId);
                    mapCsvPostCourseId.put(csvOrder, postCourseId);
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
                    product.setUpdateUser("");
                    product.setUpdateDate(new Date());
                    product.setCreateUser("");
                    product.setCreateDate(new Date());
                    product.setUnit("");
                    product.setPrice(new BigDecimal(0));
                    ((HashSet<Product>) productIterable).add(product);
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
                mapPostCourseIdProductId.put(mapCsvPostCourseId.get(csvOrder), productId);
                mapProductIdCsvOrder.put(productId + "_" + csvOrder.getOrderDate(), csvOrder);
            });
        } catch (Exception ex) {
            throw new StorageException("ファイル保存に失敗しました。", ex);//Failed to storeCode file
        }
    }

    private void setDataOrder(int tenantId) {
        if (mapError.size() == 0) {
            Map<String, String> mapKeyOrderId = new HashMap<>();

            mapProductIdCsvOrder.forEach((sProductIdOrderDate, csvOrder) -> {
                String postCourseId = mapCsvPostCourseId.get(csvOrder);
                String productId = getProductIdForKey(sProductIdOrderDate);
                String orderId = "";
                String key = postCourseId + "_" + productId + csvOrder.getOrderDate().trim();

                if (mapKeyOrderId != null && !mapKeyOrderId.isEmpty() && mapKeyOrderId.containsKey(key)) {
                    orderId = mapKeyOrderId.get(key);
                } else {
                    Order order = new Order();
                    orderId = order.getOrderId();
                    mapKeyOrderId.put(key, orderId);
                    Date date = DataUtils.converStringToDate(csvOrder.getOrderDate());
                    if (date == null)
                        date = new Date();
                    order.setOrderDate(date);

                    order.setPostCourseId(postCourseId);
                    order.setTenantId(tenantId);
                    ((HashSet<Order>) orderIterable).add(order);
                }

                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrderId(orderId);
                orderDetail.setProductId(productId);
                BigDecimal quantity = new BigDecimal(csvOrder.getQuantity());
                orderDetail.setAmount(quantity);
                orderDetail.setTenantId(tenantId);
                ((HashSet<OrderDetail>) orderDetailIterable).add(orderDetail);

            });
        }
    }

    private void saveDataMaster() throws DomainException {
        //save all storeCode
        storeRepository.saveAll(storeIterable);
        //save all postcourse
        postCourseRepository.saveAll(postCoursesIterable);
        //Save all productCode
        productRepository.saveAll(productIterable);
        //Save delivery
        deliveryRepository.saveAll(deliveryIterable);
        //save delivery detail
        deliveryDetailRepository.saveAll(deliveryDetailIterable);
    }

    private void saveDataOrder() throws DomainException {
        orderRepository.saveAll(orderIterable);
        orderDetailRepository.saveAll(orderDetailIterable);
    }
}