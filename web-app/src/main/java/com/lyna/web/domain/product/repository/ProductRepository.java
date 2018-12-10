package com.lyna.web.domain.product.repository;

import com.lyna.web.domain.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findOneByProductId(String productId);

    boolean deletebyProductId(List<String> listProductId);

    Product findOneByCode(String code);

    List<Product> findAll(int tenantId);
}
