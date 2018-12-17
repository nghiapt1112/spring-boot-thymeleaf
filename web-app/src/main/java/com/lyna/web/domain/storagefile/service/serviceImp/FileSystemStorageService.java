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
import com.lyna.web.domain.user.User;
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
    Map<String, Object> mapStorePostCode;
    Map<String, Object> mapStoreCodeCsv;
    Map<String, Object> mapStore;

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
    public List<String> store(User user, MultipartFile file, int type) throws StorageException {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        initData();
        try {
            if (file.isEmpty()) {
                throw new StorageException(
                        "空のファイルを保存出来ない。" + filename); //Failed to storeCode empty file
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
                int tenantId = user.getTenantId();
                if (type == 1) {
                    orderIterator = orderRepository.getMapOrder(reader);
                    processUpload(orderIterator);
                    setMapData(user);
                    setDataMapProduct(user);
                    setDataOrder(user);
                    saveDataMaster();
                    saveDataOrder();
                } else {
                    deliveryIterator = deliveryRepository.getMapDelivery(reader);
                    processUploadDelivery(deliveryIterator);
                    setMapDataDelivery(user);
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
        mapStorePostCode = new HashMap<>();
        mapStoreCodeCsv = new HashMap<>();
        mapStore = new HashMap<>();
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

    public void setDataMapProduct(User user) {
        //If Orderdate, Store, Post, Product,quantity exists in db => message
        Map<String, CsvOrder> mapProductIdOrder = new HashMap<>();
        mapProductIdCsvOrder.forEach((productId, csvOrder) -> {
            CsvOrder sOrder = mapProductIdCsvOrder.get(productId);
            String postCourseId = mapCsvPostCourseId.get(sOrder);
            String orderId = orderRepository.checkExists(postCourseId, sOrder.getOrderDate());
            if (orderId == null) {
                mapProductIdOrder.put(productId, csvOrder);
            }
        });

        mapProductIdCsvOrder.clear();
        mapProductIdCsvOrder = mapProductIdOrder;
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

            String keyOrder = csvOrder.getStoreCode().trim().toLowerCase() + "_" + csvOrder.getPost().trim().toLowerCase();
            String skeyCheck = keyOrder + "_" + csvOrder.getProductCode().trim().toLowerCase() + "_" + csvOrder.getOrderDate();
            if (!setOrder.contains(skeyCheck)) {
                setOrder.add(skeyCheck);

                csvOrder.setPost(csvOrder.getPost().toLowerCase().trim());
                csvOrder.setProductCode(csvOrder.getProductCode().toLowerCase().trim());
                csvOrder.setStoreCode(csvOrder.getStoreCode().toLowerCase().trim());

                mapStorePostCode.put(keyOrder, csvOrder);
                listStoreCode.add(csvOrder.getStoreCode().trim().toLowerCase());
                listProductCode.add(csvOrder.getProductCode().trim().toLowerCase());
                ListPost.add(csvOrder.getPost().trim().toLowerCase());
                mapProduct.put(csvOrder.getProductCode().trim().toLowerCase(), csvOrder);
                mapStoreCodeCsv.put(csvOrder.getStoreCode().trim().toLowerCase(), csvOrder);
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

            String keyOrder = csvDelivery.getStoreCode() + "_" + csvDelivery.getPost();
            String skeyCheck = keyOrder.trim().toLowerCase() + "_" + csvDelivery.getOrderDate().trim().toLowerCase();
            if (!setDelivery.contains(skeyCheck)) {
                setDelivery.add(skeyCheck);
                mapStorePostCode.put(keyOrder, csvDelivery);
                listStoreCode.add(csvDelivery.getStoreCode().trim().toLowerCase());
                ListPost.add(csvDelivery.getPost().trim().toLowerCase());
                mapStoreCodeCsv.put(csvDelivery.getStoreCode().trim().toLowerCase(), csvDelivery);
            }
        }
    }

    private void setMapDataDelivery(User user) throws StorageException {
        int tenantId = user.getTenantId();
        List<String> result = storeRepository.getAllByCodesAndTenantId(tenantId, listStoreCode);
        List<Store> storesInDb = storeRepository.getAll(tenantId, listStoreCode);

        List<String> stores = result.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toList());

        listStoreCode.removeIf(x -> stores.contains(x.trim().toLowerCase()));
        storesInDb.forEach(store -> {
            mapStore.put(store.getStoreId(), mapStoreCodeCsv.get(store.getCode().toLowerCase().trim()));
        });

        listStoreCode.forEach(code -> {
            CsvDelivery csvDelivery = (CsvDelivery) mapStoreCodeCsv.get(code);
            Store store = new Store();
            store.setCode(code);
            if (csvDelivery.getStoreName() != null) {
                store.setName(csvDelivery.getStoreName());
            } else
                store.setName(csvDelivery.getStoreCode());
            store.setTenantId(tenantId);
            store.setCreateUser(user.getName());
            ((HashSet<Store>) storeIterable).add(store);

            mapStore.put(store.getStoreId(), csvDelivery);
        });

        mapStore.forEach((storeId, csvDelivery) -> {
            String post = ((CsvDelivery) csvDelivery).getPost();
            String store = ((CsvDelivery) csvDelivery).getStoreCode();

            String postCourseId = postCourseRepository.checkByStoreIdAndPost(storeId, post);
            postCourseId = getGetPostCourseId(user, storeId, post, postCourseId);
            mapCsvPostCourseId.put(mapStorePostCode.get(store + "_" + post), postCourseId);
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
                    delivery.setCreateUser(user.getName());
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
                    deliveryDetail.setCreateUser(user.getName());
                    deliveryDetail.setUpdateDate(new Date());
                    deliveryDetail.setUpdateUser("");
                    ((HashSet<DeliveryDetail>) deliveryDetailIterable).add(deliveryDetail);
                }
            });
        });
    }

    private String getGetPostCourseId(User user, String storeId, String post, String postCourseId) {
        if (postCourseId == null) {
            PostCourse postCourse = new PostCourse();
            postCourse.setPost(post);
            postCourse.setStoreId(storeId);
            postCourse.setTenantId(user.getTenantId());
            postCourse.setCreateUser(user.getName());
            postCourseId = postCourse.getPostCourseId();
            ((HashSet<PostCourse>) postCoursesIterable).add(postCourse);
        }
        return postCourseId;
    }

    private void setMapData(User user) throws StorageException {
        try {
            int tenantId = user.getTenantId();
            List<String> result = storeRepository.getAllByCodesAndTenantId(tenantId, listStoreCode);
            List<Store> storesInDb = storeRepository.getAll(tenantId, listStoreCode);
            List<String> resultProducts = productRepository.getListProductCodeByProductCode(tenantId, listProductCode);
            List<Product> productInDB = productRepository.getProductsByProductCode(tenantId, listProductCode);

            List<String> stores = result.stream()
                    .map(String::toLowerCase)
                    .collect(Collectors.toList());

            List<String> products = resultProducts.stream()
                    .map(String::toLowerCase)
                    .collect(Collectors.toList());

            listProductCode.removeIf(x -> products.contains(x));

            listStoreCode.removeIf(x -> stores.contains(x));

            storesInDb.forEach(store -> {
                mapStore.put(store.getStoreId(), mapStoreCodeCsv.get(store.getCode().trim().toLowerCase()));
            });

            listStoreCode.forEach(code -> {
                CsvOrder csvOrder = (CsvOrder) mapStoreCodeCsv.get(code);
                Store store = new Store();
                store.setCode(code);
                store.setName(csvOrder.getStoreName());
                store.setTenantId(tenantId);
                ((HashSet<Store>) storeIterable).add(store);
                store.setCreateUser(user.getName());

                mapStore.put(store.getStoreId(), csvOrder);
            });

            mapStore.forEach((storeId, csvOrder) -> {
                String post = ((CsvOrder) csvOrder).getPost();
                String store = ((CsvOrder) csvOrder).getStoreCode();

                String postCourseId = postCourseRepository.checkByStoreIdAndPost(storeId, post);
                postCourseId = getGetPostCourseId(user, storeId, post, postCourseId);
                mapCsvPostCourseId.put(mapStorePostCode.get(store + "_" + post), postCourseId);
            });

            listProductCode.forEach(code -> {
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
                product.setCreateUser(user.getName());
                product.setCreateDate(new Date());
                product.setUnit("");
                product.setPrice(new BigDecimal(0));
                ((HashSet<Product>) productIterable).add(product);
                mapPostCourseIdProductId.put(mapCsvPostCourseId.get(csvOrder), product.getProductId());
                mapProductIdCsvOrder.put(product.getProductId(), csvOrder);
            });

            productInDB.forEach(product -> {
                String productCode = product.getCode();
                CsvOrder csvOrder = mapProduct.get(productCode);
                mapPostCourseIdProductId.put(mapCsvPostCourseId.get(csvOrder), product.getProductId());
                mapProductIdCsvOrder.put(product.getProductId(), csvOrder);
            });
        } catch (Exception ex) {
            throw new StorageException("ファイル保存に失敗しました。", ex);//Failed to storeCode file
        }
    }

    private void setDataOrder(User user) {
        int tenantId = user.getTenantId();
        if (mapError.size() == 0) {
            Map<String, String> mapKeyOrderId = new HashMap<>();
            HashSet<String> setOrderDetail = new HashSet<>();

            mapProductIdCsvOrder.forEach((productId, csvOrder) -> {
                CsvOrder sOrder = mapProductIdCsvOrder.get(productId);
                String postCourseId = mapCsvPostCourseId.get(sOrder);
                String orderId = "";
                if (!setOrderDetail.contains(postCourseId + "_" + productId)) {
                    setOrderDetail.add(postCourseId + "_" + productId);
                    if (mapKeyOrderId != null && !mapKeyOrderId.isEmpty() && mapKeyOrderId.containsKey(postCourseId)) {
                        orderId = mapKeyOrderId.get(postCourseId);
                    } else {
                        Order order = new Order();
                        orderId = order.getOrderId();
                        mapKeyOrderId.put(postCourseId, orderId);

                        Date date = DataUtils.converStringToDate(sOrder.getOrderDate());
                        if (date == null)
                            date = new Date();
                        order.setOrderDate(date);
                        order.setCreateUser(user.getName());
                        order.setPostCourseId(postCourseId);
                        order.setTenantId(tenantId);
                        ((HashSet<Order>) orderIterable).add(order);
                    }

                    OrderDetail orderDetail = new OrderDetail();
                    orderDetail.setOrderId(orderId);
                    orderDetail.setProductId(productId);
                    BigDecimal quantity = new BigDecimal(sOrder.getQuantity());
                    orderDetail.setAmount(quantity);
                    orderDetail.setTenantId(tenantId);
                    ((HashSet<OrderDetail>) orderDetailIterable).add(orderDetail);
                }
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