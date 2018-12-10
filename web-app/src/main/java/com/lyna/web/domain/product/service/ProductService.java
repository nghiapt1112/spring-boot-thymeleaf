package com.lyna.web.domain.product.service;

import com.lyna.web.domain.product.Product;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.List;

public interface ProductService {
    void update(Product product, UsernamePasswordAuthenticationToken principal);
    Product findOneByProductId(String productId);

    List<Product> findByTenantId(int tenantId);
    void create(Product product, UsernamePasswordAuthenticationToken principal);
    boolean deleteByProductIds(List<String> productIds);
    Product findOneByCode(String code);
}
