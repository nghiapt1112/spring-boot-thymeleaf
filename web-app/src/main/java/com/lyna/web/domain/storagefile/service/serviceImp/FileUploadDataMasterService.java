package com.lyna.web.domain.storagefile.service.serviceImp;

import com.lyna.commons.infrustructure.service.BaseService;
import com.lyna.web.domain.mpackage.Package;
import com.lyna.web.domain.mpackage.repository.PackageRepository;
import com.lyna.web.domain.product.Product;
import com.lyna.web.domain.product.repository.ProductRepository;
import com.lyna.web.domain.storagefile.StorageProperties;
import com.lyna.web.domain.storagefile.exeption.StorageException;
import com.lyna.web.domain.storagefile.service.StorageService;
import com.lyna.web.domain.stores.Store;
import com.lyna.web.domain.stores.repository.StoreRepository;
import com.lyna.web.domain.user.User;
import com.lyna.web.domain.view.CsvPackage;
import com.lyna.web.domain.view.CsvProduct;
import com.lyna.web.domain.view.CsvStore;
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
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
public class FileUploadDataMasterService extends BaseService implements StorageService {

    private final Path rootLocation;

    List<String> listStoreCode;
    List<String> listProductCode;
    List<String> listPackageName;
    Set<Product> productIterable;
    Map<String, Object> mapData;

    Set<Store> storeIterable;
    Set<Package> packageIterable;
    Map<Integer, String> mapError;

    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PackageRepository packageRepository;

    @Autowired
    public FileUploadDataMasterService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }

    @Override
    @Transactional
    public Map<Integer, String> store(User user, MultipartFile file, int type) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        if (file.isEmpty()) {
            mapError.put(500, "空のファイルを保存出来ない。" + filename);
        }

        if (filename.contains("..")) {
            mapError.put(500, "現在のディレクトリの外に相対パスを持つファイルを保存できません");
        }

        try (InputStream inputStream = file.getInputStream()) {
            Reader reader = new InputStreamReader(inputStream);
            mapData = new HashMap<>();

            if (type == 3) {
                innitDataStore();
                if (mapError.size() == 0) {
                    Iterator<CsvStore> storeIterator = storeRepository.getMapStore(reader);
                    processUploadStore(storeIterator);
                }
                setDataStore(user);
            } else if (type == 4) {
                innitDataProduct();
                if (mapError.size() == 0) {
                    Iterator<CsvProduct> productIterator = productRepository.getMapStore(reader);
                    processUploadProduct(productIterator);
                }
                setDataProduct(user);
            } else if (type == 5) {
                innitDataPackage();
                if (mapError.size() == 0) {
                    Iterator<CsvPackage> packageIterator = packageRepository.getMapStore(reader);
                    processUploadPackage(packageIterator);
                }
                setDataPackage(user);
            }
        } catch (Exception ex) {
            mapError.put(500, "ファイル保存に失敗しました。");
        }
        return mapError;
    }

    private void setDataPackage(User currentUser) {
        listPackageName = listPackageName.stream().distinct().collect(Collectors.toList());

        List<Package> packagesInDb = packageRepository.getAllByNameAndTenantId(listPackageName, currentUser.getTenantId());

        List<String> existedPackageNames = packagesInDb.stream().map(t -> t.getName().toLowerCase()).collect(Collectors.toList());

        List<Package> packages = new ArrayList<>();
        for (String packageName : listPackageName) {
            if (!existedPackageNames.contains(packageName)) {
                CsvPackage csvPackage = (CsvPackage) mapData.get(packageName);
                packages.add(csvPackage.createPackage(currentUser));
            }
        }
        for (Package p : packagesInDb) {
            CsvPackage csvPackage = (CsvPackage) mapData.get(p.getName().toLowerCase());
            if (csvPackage != null) {
                p.setName(csvPackage.getPackageName());
                p.setUnit(csvPackage.getUnit());
                p.setEmptyWeight(csvPackage.getEmptyWeight());
                p.setFullLoadWeight(csvPackage.getFullLoadWeight());
                p.setEmptyCapacity(csvPackage.getEmptyCapacity());
                p.setFullLoadCapacity(csvPackage.getFullLoadCapacity());
                p.setTenantId(currentUser.getTenantId());
                p.setUpdateUser(currentUser.getId());
                p.setUpdateDate(new Date());
                packageIterable.add(p);
            }
        }
        packageRepository.saveAll(packages);
    }


    private void setDataProduct(User currentUser) {
        listProductCode = listProductCode.stream().distinct().collect(Collectors.toList());

        List<Product> productsInDb = productRepository.getProductsByProductCode(currentUser.getTenantId(), listProductCode);

        List<String> existedProductCodes = productsInDb.stream().map(t -> t.getCode()).collect(Collectors.toList());

        List<Product> products = new ArrayList<>();
        for (String productCode : listProductCode) {
            if (!existedProductCodes.contains(productCode)) {
                CsvProduct csvProduct = (CsvProduct) mapData.get(productCode);
                products.add(csvProduct.createProduct(currentUser));
            }
        }
        for (Product product : productsInDb) {
            CsvProduct csvProduct = (CsvProduct) mapData.get(product.getCode().toLowerCase());
            if (csvProduct != null) {
                product.setCode(csvProduct.getProductCode());
                product.setName(csvProduct.getProductName());
                product.setUnit(csvProduct.getUnit());
                product.setPrice(csvProduct.getUnitPrice());
                product.setOrderDetails(product.getOrderDetails());
                product.setCategory1(csvProduct.getCategory1());
                product.setCategory2(csvProduct.getCategory2());
                product.setCategory3(csvProduct.getCategory3());
                product.setTenantId(currentUser.getTenantId());
                product.setUpdateUser(currentUser.getId());
                product.setUpdateDate(new Date());
                productIterable.add(product);
            }
        }
        productRepository.saveAll(products);
    }


    private void setDataStore(User currentUser) {
        listStoreCode = listStoreCode.stream().distinct().collect(Collectors.toList());

        List<Store> storesInDb = storeRepository.getAll(currentUser.getTenantId(), listStoreCode);

        List<String> existedStoreCodes = storesInDb.stream().map(t -> t.getCode()).collect(Collectors.toList());

        List<Store> stores = new ArrayList<>();
        for (String storeCode : listStoreCode) {
            if (!existedStoreCodes.contains(storeCode)) {
                CsvStore csvStore = (CsvStore) mapData.get(storeCode);
                stores.add(csvStore.createStore(currentUser));
            }
        }
        for (Store store : storesInDb) {
            CsvStore csvStore = (CsvStore) mapData.get(store.getCode());
            if (csvStore != null) {
                store.setName(csvStore.getStoreName());
                store.setArea(csvStore.getArea());
                store.setMajorArea(csvStore.getMajorArea());
                store.setPhoneNumber(csvStore.getPhoneNumber());
                store.setPersonCharge(csvStore.getPersonInCharge());
                store.setPostCourses(store.getPostCourses());
                store.setTenantId(currentUser.getTenantId());
                store.setUpdateUser(currentUser.getId());
                store.setUpdateDate(new Date());
                storeIterable.add(store);
            }
        }

        storeRepository.saveAll(stores);
    }

    void innitDataStore() {

        storeIterable = new HashSet<>();
        listStoreCode = new ArrayList<>();
    }

    void innitDataProduct() {

        productIterable = new HashSet<>();
        listProductCode = new ArrayList<>();
    }

    void innitDataPackage() {

        packageIterable = new HashSet<>();
        listPackageName = new ArrayList<>();
    }

    private void processUploadPackage(Iterator<CsvPackage> packageIterator) {
        while (packageIterator.hasNext()) {
            CsvPackage csvPackage = packageIterator.next();
            int row = 1;
            if (csvPackage.getPackageName() == null
                    || csvPackage.getPackageName().isEmpty()
                    || csvPackage.getUnit() == null
                    || csvPackage.getUnit().isEmpty()
                    || csvPackage.getEmptyWeight() == null
                    || csvPackage.getFullLoadWeight() == null
                    || csvPackage.getEmptyCapacity() == null
                    || csvPackage.getFullLoadCapacity() == null

            ) {
                mapError.put(500, "行目 " + row + " にデータが不正");
            }
            row++;

            mapData.put(csvPackage.getPackageName().toLowerCase().trim(), csvPackage);
            listPackageName.add(csvPackage.getPackageName().toLowerCase().trim());

        }
    }

    private void processUploadProduct(Iterator<CsvProduct> productIterator) {
        while (productIterator.hasNext()) {
            CsvProduct csvProduct = productIterator.next();
            int row = 1;
            if (csvProduct.getProductCode() == null
                    || csvProduct.getProductCode().isEmpty()
                    || csvProduct.getProductName() == null
                    || csvProduct.getProductName().isEmpty()
                    || csvProduct.getUnit() == null
                    || csvProduct.getUnit().isEmpty()
                    || csvProduct.getUnitPrice() == null
                    || csvProduct.getCategory1() == null
                    || csvProduct.getCategory1().isEmpty()
                    || csvProduct.getCategory2() == null
                    || csvProduct.getCategory2().isEmpty()
                    || csvProduct.getCategory3() == null
                    || csvProduct.getCategory3().isEmpty()

            ) {
                mapError.put(500, "行目 " + row + " にデータが不正");
            }
            row++;

            mapData.put(csvProduct.getProductCode().trim(), csvProduct);
            listProductCode.add(csvProduct.getProductCode());

        }
    }

    private void processUploadStore(Iterator<CsvStore> storeIterator) {
        while (storeIterator.hasNext()) {
            CsvStore csvStore = storeIterator.next();
            int row = 1;
            if (csvStore.getPost() == null
                    || csvStore.getPost().isEmpty()
                    || csvStore.getStoreName().isEmpty()
                    || csvStore.getStoreName() == null
                    || csvStore.getPersonInCharge().isEmpty()
                    || csvStore.getPersonInCharge() == null
                    || csvStore.getPhoneNumber() == null
                    || csvStore.getPhoneNumber().isEmpty()
                    || csvStore.getAddress() == null
                    || csvStore.getAddress().isEmpty()
                    || csvStore.getArea() == null
                    || csvStore.getMajorArea().isEmpty()
                    || csvStore.getStoreCode() == null
                    || csvStore.getStoreCode().isEmpty()

            ) {
                mapError.put(500, "行目 " + row + " にデータが不正");
            }
            row++;

            mapData.put(csvStore.getStoreCode().toLowerCase().trim(), csvStore);
            listStoreCode.add(csvStore.getStoreCode());

        }
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
}
