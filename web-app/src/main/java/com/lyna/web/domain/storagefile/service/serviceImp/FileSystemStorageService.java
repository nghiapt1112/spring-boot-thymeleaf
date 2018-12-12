package com.lyna.web.domain.storagefile.service.serviceImp;


import com.lyna.commons.infrustructure.exception.DomainException;
import com.lyna.commons.infrustructure.service.BaseService;
import com.lyna.commons.utils.DataUtils;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;

@Service
public class FileSystemStorageService extends BaseService implements StorageService {

    private final Path rootLocation;
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
    List<String> listStoreCode;
    List<String> listProductCode;
    List<String> ListPost;
    Map<String, CsvOrder> mapProduct;
    Map<String, CsvOrder> mapStorePostCode;
    Map<String, CsvOrder> mapStoreOrder;
    Map<String, CsvOrder> mapStore;
    Iterable<Product> productIterable;
    Iterable<Store> storeIterable;
    Iterable<PostCourse> postCoursesIterable;

    Iterable<Order> orderIterable;
    Iterable<OrderDetail> orderDetailIterable;

    Map<CsvOrder, String> mapCsvPostCourseId;
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
    public FileSystemStorageService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> store(int tenantId, MultipartFile file) throws StorageException {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        initData();
        try {
            if (file.isEmpty()) {
                throw new StorageException("空のファイルを保存出来ない。" + filename); //Failed to store empty file
            }
            if (filename.contains("..")) {
                // This is a security check
                throw new StorageException(
                        "現在のディレクトリの外に相対パスを持つファイルを保存できません。"//Cannot store file with relative path outside current directory
                                + filename);
            }

            try (InputStream inputStream = file.getInputStream()) {
                Reader reader = new InputStreamReader(inputStream);
                Iterator<CsvOrder> orderIterator = orderRepository.getMapOrder(reader);
                processUpload(orderIterator);
                setMapData(tenantId);
                setDataMapProduct();
                setDataOrder(tenantId);
                saveDataMaster();
                saveDataOrder();
            }
        } catch (IOException e) {
            throw new StorageException("ファイル保存に失敗しました。" + filename, e);//Failed to store file
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
        mapStoreOrder = new HashMap<>();
        mapStore = new HashMap<>();
        productIterable = new HashSet<>();
        storeIterable = new HashSet<>();
        postCoursesIterable = new HashSet<>();

        orderIterable = new HashSet<>();
        orderDetailIterable = new HashSet<>();

        mapCsvPostCourseId = new HashMap<>();
        mapPostCourseIdProductId = new HashMap<>();
        mapProductIdCsvOrder = new HashMap<>();
        mapError = new ArrayList<>();
    }

    public void setDataMapProduct() {
        //If Orderdate, Store, Post, Product,quantity exists in db => message
        Map<String, CsvOrder> mapProductIdOrder = new HashMap<>();
        mapProductIdCsvOrder.forEach((productId, csvOrder) -> {
            CsvOrder sOrder = mapProductIdCsvOrder.get(productId);
            String postCourseId = mapCsvPostCourseId.get(sOrder);
            boolean isExists = orderRepository.checkExists(postCourseId, productId, csvOrder.getQuantity());
            if (!isExists) {
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

            if (csvOrder.getPost().isEmpty()) {
                mapError.add("行目 " + row + " にデータが不正");
            }
            if (csvOrder.getQuantity().isEmpty()) {
                mapError.add("行目 " + row + " にデータが不正");
            }
            if (csvOrder.getStore().isEmpty()) {
                mapError.add("行目 " + row + " にデータが不正");
            }
            if (csvOrder.getProduct().isEmpty()) {
                mapError.add("行目 " + row + " にデータが不正");
            }
            if (!DataUtils.isNumeric(csvOrder.getQuantity())) {
                mapError.add("数量は数字データではない");
            }

            row++;

            String keyOrder = csvOrder.getStore() + "_" + csvOrder.getPost();
            String skeyCheck = keyOrder + "_" + csvOrder.getProduct();
            if (!setOrder.contains(skeyCheck)) {
                setOrder.add(skeyCheck);
                mapStorePostCode.put(keyOrder, csvOrder);
                listStoreCode.add(csvOrder.getStore());
                listProductCode.add(csvOrder.getProduct());
                ListPost.add(csvOrder.getPost());
                mapProduct.put(csvOrder.getProduct(), csvOrder);
                mapStoreOrder.put(csvOrder.getStore(), csvOrder);
            }
        }
    }

    private void setMapData(int tenantId) throws StorageException {
        try {
            List<String> stores = storeRepository.getAllByCodesAndTenantId(tenantId, listStoreCode);
            List<Store> storesInDb = storeRepository.getAll(tenantId, listStoreCode);
            List<String> products = productRepository.getListProductCodeByProductCode(tenantId, listProductCode);
            List<Product> productInDB = productRepository.getProductsByProductCode(tenantId, listProductCode);

            listStoreCode.removeIf(x -> stores.contains(x));
            listProductCode.removeIf(x -> products.contains(x));

            storesInDb.forEach(store -> {
                mapStore.put(store.getStoreId(), mapStoreOrder.get(store.getCode()));
            });

            listStoreCode.forEach(code -> {
                CsvOrder csvOrder = mapStoreOrder.get(code);
                Store store = new Store();
                store.setCode(code);
                store.setName(code);
                store.setTenantId(tenantId);
                ((HashSet<Store>) storeIterable).add(store);

                mapStore.put(store.getStoreId(), csvOrder);
            });

            mapStore.forEach((storeId, csvOrder) -> {
                String postCourseId = postCourseRepository.checkByStoreIdAndPost(storeId, csvOrder.getPost());
                if (postCourseId == null) {
                    PostCourse postCourse = new PostCourse();
                    postCourse.setPost(csvOrder.getPost());
                    postCourse.setStoreId(storeId);
                    postCourse.setTenantId(tenantId);
                    postCourseId = postCourse.getPostCourseId();
                    ((HashSet<PostCourse>) postCoursesIterable).add(postCourse);
                }
                mapCsvPostCourseId.put(mapStorePostCode.get(csvOrder.getStore() + "_" + csvOrder.getPost()), postCourseId);
            });

            listProductCode.forEach(code -> {
                CsvOrder csvOrder = mapProduct.get(code);
                Product product = new Product();
                product.setCode(code);
                product.setName(code);
                product.setCategory1(csvOrder.getCategory1());
                product.setCategory2(csvOrder.getCategory2());
                product.setCategory3(csvOrder.getCategory3());
                product.setTenantId(tenantId);

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
            throw new StorageException("ファイル保存に失敗しました。", ex);//Failed to store file
        }
    }

    private void setDataOrder(int tenantId) {
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

                        try {
                            Date date = formatter.parse(sOrder.getOrderDate());
                            order.setOrderDate(date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

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
        //save all store
        storeRepository.saveAll(storeIterable);
        //save all postcourse
        postCourseRepository.saveAll(postCoursesIterable);
        //Save all product
        productRepository.saveAll(productIterable);
    }

    private void saveDataOrder() throws DomainException {
        orderRepository.saveAll(orderIterable);
        orderDetailRepository.saveAll(orderDetailIterable);
    }
}