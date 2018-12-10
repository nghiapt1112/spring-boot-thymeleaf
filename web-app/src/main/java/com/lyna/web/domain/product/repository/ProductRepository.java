package com.lyna.web.domain.product.repository;

import com.lyna.web.domain.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findOneByProductId(String productId);

    boolean deleteByProductIds(List<String> productIds);

    Product findOneByCode(String code);

    List<Product> findByTenantId(int tenantId);
}

