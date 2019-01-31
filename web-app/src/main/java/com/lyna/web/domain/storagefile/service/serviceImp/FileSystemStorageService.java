package com.lyna.web.domain.storagefile.service.serviceImp;

import com.lyna.commons.infrustructure.exception.DomainException;
import com.lyna.commons.infrustructure.service.BaseService;
import com.lyna.commons.utils.Constants;
import com.lyna.web.domain.AI.AIService;
import com.lyna.web.domain.order.Order;
import com.lyna.web.domain.order.OrderDetail;
import com.lyna.web.domain.order.repository.OrderDetailRepository;
import com.lyna.web.domain.order.service.OrderService;
import com.lyna.web.domain.postCourse.PostCourse;
import com.lyna.web.domain.postCourse.repository.PostCourseRepository;
import com.lyna.web.domain.product.Product;
import com.lyna.web.domain.product.repository.ProductRepository;
import com.lyna.web.domain.storagefile.StorageProperties;
import com.lyna.web.domain.storagefile.exeption.StorageException;
import com.lyna.web.domain.storagefile.repository.FileRepository;
import com.lyna.web.domain.storagefile.service.StorageService;
import com.lyna.web.domain.stores.Store;
import com.lyna.web.domain.stores.repository.StoreRepository;
import com.lyna.web.domain.user.User;
import com.lyna.web.domain.view.CsvOrder;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
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
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.lyna.commons.utils.DataUtils.isNumeric;
import static com.lyna.commons.utils.DateTimeUtils.converStringToDate;

@Service
public class FileSystemStorageService extends BaseService implements StorageService {

    private final Path rootLocation;
    Map<String, String> setStoreCodePost;
    private String READ_FILE_FAILED = "err.csv.readFileFailed.msg";
    private List<String> listStoreCode;
    private List<String> listProductCode;
    private List<String> ListPost;
    private Map<String, CsvOrder> mapProductCodeCsv;
    private Map<String, CsvOrder> mapKeyCsv;

    private Map<String, Object> mapStoreCodeCsv;
    private Map<Object, String> mapCsvPostCourseId;

    private Map<String, CsvOrder> mapProductIdCsvOrder;
    private Map<Integer, String> mapError;

    private Set<Product> productIterable;
    private Set<Store> storeIterable;
    private Set<PostCourse> postCoursesIterable;


    private Set<Order> orderIterable;
    private Set<OrderDetail> orderDetailIterable;
    private List<String> orderIds;

    @Autowired
    private OrderService orderService;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private PostCourseRepository postCourseRepository;


    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private AIService aiService;

    @Autowired
    public FileSystemStorageService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    @Override
    public String store(MultipartFile file) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
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
                return filename;
            }
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + filename, e);
        }
    }

    @Override
    public Map<Integer, String> store(User user, String fileName, InputStream inputStream, String typeUploadFile, Map<Integer, String> mapHeader) {
        try {
            int tenantId = user.getTenantId();
            String userId = user.getId();
            initDataOrder();
            Reader reader = new InputStreamReader(inputStream);
            Iterator<CsvOrder> orderIterator = orderService.getMapOrder(reader, mapHeader);
            processUpload(orderIterator);
            int status = 0;

            if (mapError.isEmpty()) {
                setMapData(tenantId, userId, typeUploadFile);
                setDataOrder(tenantId, userId, typeUploadFile);

                if (!productIterable.isEmpty() || !orderIterable.isEmpty() || !orderDetailIterable.isEmpty())
                    saveDataOrder();

                if (mapError.size() == 0) {
                    status = aiService.calculateLogisticsWithAI(user, orderIds);

                    if (status == Constants.AI_STATUS.EMPTY || status == Constants.AI_STATUS.ERROR)
                        mapError.put(500, "解析に失敗しました。");
                }
            }
        } catch (Exception ex) {
            mapError.put(500, "Error :" + ex.getCause() + ", " + ex.getMessage() + ", " + ex.getStackTrace());
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
    public Resource loadAsFileResource(String filename) {
        //Gets the XML file under src/main/resources folder
        Resource resource = new ClassPathResource(filename);
        if (resource.exists() || resource.isReadable()) {
            return resource;
        } else {
            throw new StorageException("Could not read file: " + filename);
        }
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
            throw new StorageException(toStr(READ_FILE_FAILED) + filename, e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public Map<String, Integer> getMapHeader(MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            Reader reader = new InputStreamReader(inputStream);
            Map<String, Integer> headerOrder = orderService.getHeaderOrder(reader);
            return headerOrder;
        } catch (Exception ex) {
            mapError.put(toInteger("err.csv.saveFileFailed.code"), toStr("err.csv.saveFileFailed.msg"));
        }
        return null;
    }

    @Override
    public List<CSVRecord> getMapData(MultipartFile file) {
        List<CSVRecord> recordList = null;
        try (InputStream inputStream = file.getInputStream()) {
            Reader reader = new InputStreamReader(inputStream);
            recordList = orderService.getDataOrder(reader);
            return recordList;
        } catch (Exception ex) {
            mapError.put(500, toStr(READ_FILE_FAILED));
        }
        return recordList;
    }

    @Override
    public Map<Integer, String> getMapHeader() {
        final Resource resource = loadAsFileResource(Constants.HEADER_FILE_ORDER);
        try {
            return fileRepository.readFileHeader(resource.getInputStream());
        } catch (IOException e) {
            throw new StorageException(toStr(READ_FILE_FAILED));
        }
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }

    void initDataOrder() {
        listStoreCode = new ArrayList<>();
        ListPost = new ArrayList<>();
        mapStoreCodeCsv = new HashMap<>();
        storeIterable = new HashSet<>();
        postCoursesIterable = new HashSet<>();
        mapCsvPostCourseId = new HashMap<>();
        mapError = new HashMap<>();
        setStoreCodePost = new HashMap<>();
        orderIds = new ArrayList<>();
        listProductCode = new ArrayList<>();
        mapProductCodeCsv = new HashMap<>();
        mapKeyCsv = new HashMap<>();
        productIterable = new HashSet<>();
        orderIterable = new HashSet<>();
        orderDetailIterable = new HashSet<>();
        mapProductIdCsvOrder = new HashMap<>();
    }


    private String getByProductIdForKey(String dataSplit) {
        String[] sProductIdOrderDate = dataSplit.split("#");
        return sProductIdOrderDate[0];
    }

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

            String keyOrder = csvOrder.getStoreCode().trim().toLowerCase() + "#" + csvOrder.getPost().trim().toLowerCase() + "#" + csvOrder.getOrderDate();
            String sKeyOrderCheck = keyOrder + "#" + csvOrder.getProductCode().trim().toLowerCase();
            if (!setOrder.contains(sKeyOrderCheck)) {
                setOrder.add(sKeyOrderCheck);
                csvOrder.setPost(csvOrder.getPost().toLowerCase().trim());
                csvOrder.setProductCode(csvOrder.getProductCode().toLowerCase().trim());
                csvOrder.setStoreCode(csvOrder.getStoreCode().toLowerCase().trim());
                mapStoreCodeCsv.put(csvOrder.getStoreCode().toLowerCase().trim(), csvOrder);
                listStoreCode.add(csvOrder.getStoreCode().trim().toLowerCase());
                listProductCode.add(csvOrder.getProductCode().trim().toLowerCase());
                ListPost.add(csvOrder.getPost().trim().toLowerCase());
                mapProductCodeCsv.put(csvOrder.getProductCode().trim().toLowerCase(), csvOrder);
                mapKeyCsv.put(sKeyOrderCheck, csvOrder);
            }
        }
    }


    private void setMapStorePostCourse(int tenantId, Object csvData, String post, String skey, String storeId, String userId) {
        String postCourseId = postCourseRepository.findByStoreIdAndPost(storeId, post);
        if (postCourseId == null)
            postCourseId = getPostCourseId(tenantId, storeId, post, postCourseId, userId);
        setStoreCodePost.put(skey, postCourseId);
        mapCsvPostCourseId.put(csvData, postCourseId);
    }

    private String getPostCourseId(int tenantId, String storeId, String post, String postCourseId, String userId) {
        PostCourse postCourse = new PostCourse(tenantId, storeId, post, userId);
        postCoursesIterable.add(postCourse);
        return postCourse.getPostCourseId();
    }

    private void setMapData(int tenantId, String userId, String typeUploadFile) throws StorageException {
        try {
            List<String> result = storeRepository.getAllByCodesAndTenantId(tenantId, listStoreCode);
            List<Store> storesInDb = storeRepository.getAll(tenantId, listStoreCode);
            List<String> resultProducts = productRepository.getListProductCodeByProductCode(tenantId, listProductCode);
            List<Product> productInDB = productRepository.getProductsByProductCode(tenantId, listProductCode);
            Map<String, String> mapProductCodeProductId = new HashMap<>();
            Map<String, String> mapStoreCodeStoreId = new HashMap<>();
            Set<String> setStoreCode = new HashSet<>();
            Set<String> setProductCode = new HashSet<>();

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
                    CsvOrder csvOrder = (CsvOrder) mapStoreCodeCsv.get(code);
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

            mapKeyCsv.forEach((storeCodeOrderDate, csvOrder) -> {
                String post = (csvOrder).getPost();
                String storeCode = (csvOrder).getStoreCode();
                String keyStoreCodePost = storeCode + "#" + post;
                if (!setStoreCodePost.containsKey(keyStoreCodePost)) {
                    String storeId = mapStoreCodeStoreId.get(storeCode);
                    setMapStorePostCourse(tenantId, csvOrder, post, keyStoreCodePost, storeId, userId);
                } else {
                    mapCsvPostCourseId.put(csvOrder, setStoreCodePost.get(keyStoreCodePost));
                }
            });

            listProductCode.forEach(code -> {
                if (!setProductCode.contains(code)) {
                    setProductCode.add(code);
                    CsvOrder csvOrder = mapProductCodeCsv.get(code);
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
                if (typeUploadFile.equals(Constants.UPDATE_DATA)) {
                    CsvOrder csvOrder = mapProductCodeCsv.get(productCode);
                    Product productUpdate = product;
                    productUpdate.setCategory1(csvOrder.getCategory1());
                    productUpdate.setCategory2(csvOrder.getCategory2());
                    productUpdate.setCategory3(csvOrder.getCategory3());
                    productUpdate.setUpdateDate(new Date());
                    productUpdate.setUpdateUser(userId);
                    productIterable.add(productUpdate);
                }
            });

            mapKeyCsv.forEach((sProductCodeOderDate, csvOrder) -> {
                String productCode = csvOrder.getProductCode();
                String productId = mapProductCodeProductId.get(productCode);
                mapProductIdCsvOrder.put(productId + "#" + csvOrder.getOrderDate() + "#" + csvOrder.getStoreCode().trim().toLowerCase() + "#" + csvOrder.getPost().trim().toLowerCase(), csvOrder);
            });
        } catch (Exception ex) {
            mapError.put(500, toStr("err.csv.saveFileFailed.msg"));
        }
    }

    private void setDataOrder(int tenantId, String userId, String typeUploadFile) {
        if (mapError.size() == 0) {
            Map<String, String> mapKeyOrderId = new HashMap<>();

            mapProductIdCsvOrder.forEach((sProductIdOrderDate, csvOrder) -> {
                String postCourseId = mapCsvPostCourseId.get(csvOrder);
                String productId = getByProductIdForKey(sProductIdOrderDate);
                String key = postCourseId + "#" + csvOrder.getOrderDate().trim();
                String orderId = orderService.getOrderIdByPostCourseIdAndTenantId(postCourseId, csvOrder.getOrderDate().trim(), tenantId);
                if (orderId != null) {
                    List<OrderDetail> orderDetails = orderDetailRepository.findByOrderIdAndProductIdAndTenantId(orderId, productId, tenantId);
                    if (orderDetails != null) {
                        if (typeUploadFile.equals(Constants.UPDATE_DATA)) {
                            orderDetails.forEach(orderDetail -> {
                                orderDetail.setUpdateDate(new Date());
                                orderDetail.setUpdateUser(userId);
                                orderDetail.setAmount(new BigDecimal(csvOrder.getQuantity()));
                                orderDetailIterable.add(orderDetail);
                            });
                        }
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
                        if (date == null) {
                            date = new Date();
                        }

                        order.setOrderDate(date);
                        order.setCreateDate(date);
                        order.setCreateUser(userId);

                        order.setPostCourseId(postCourseId);
                        order.setTenantId(tenantId);
                        orderIterable.add(order);
                    }
                    setOrderDetailIterable(orderId, productId, csvOrder, tenantId, userId);
                }

                if (!orderIds.contains(orderId))
                    orderIds.add(orderId);
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


    void saveDataMaster() throws DomainException {
        //save all storeCode
        if (!storeIterable.isEmpty())
            storeRepository.saveAll(storeIterable);
        //save all postcourse
        if (!postCoursesIterable.isEmpty())
            postCourseRepository.saveAll(postCoursesIterable);
    }


    @Transactional
    void saveDataOrder() throws DomainException {
        if (!storeIterable.isEmpty() || !postCoursesIterable.isEmpty())
            saveDataMaster();
        //Save all productCode
        if (!productIterable.isEmpty())
            productRepository.saveAll(productIterable);
        if (!orderIterable.isEmpty())
            orderService.saveAll(orderIterable);
        if (!orderDetailIterable.isEmpty())
            orderDetailRepository.saveAll(orderDetailIterable);
    }
}