package com.lyna.web.domain.product.repository;

import com.lyna.web.domain.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, String> {
    Product findOneByProductIdAndTenantId(String productId, int tenantId);

    boolean deleteByProductIdsAndTenantId(List<String> productIds, int tenantId);

    Product findOneByCodeAndTenantId(String code, int tenantId);

    List<Product> findByTenantId(int tenantId);

    List<String> getListProductCodeByProductCode(int tenantId, List<String> products);

    List<Product> getProductsByProductCode(int tenantId, List<String> products);

}


