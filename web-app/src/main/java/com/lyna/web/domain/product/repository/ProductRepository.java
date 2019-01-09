package com.lyna.web.domain.product.repository;

import com.lyna.web.domain.product.Product;
import com.lyna.web.domain.view.CsvProduct;
import com.lyna.web.domain.view.CsvStore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Reader;
import java.util.Iterator;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, String> {
    Iterator<CsvProduct> getMapStore(Reader targetReader);

    Product findOneByProductIdAndTenantId(String productId, int tenantId);

    boolean deleteByProductIdsAndTenantId(List<String> productIds, int tenantId);

    Product findOneByCodeAAndTenantId(String code, int tenantId);

    List<Product> findByTenantId(int tenantId);

    List<String> getListProductCodeByProductCode(int tenantId, List<String> products);

    List<Product> getProductsByProductCode(int tenantId, List<String> products);

}


