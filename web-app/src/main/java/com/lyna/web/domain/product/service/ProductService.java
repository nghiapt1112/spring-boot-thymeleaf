package com.lyna.web.domain.product.service;

import com.lyna.web.domain.product.Product;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.List;

public interface ProductService {
    void updateProduct(Product product, UsernamePasswordAuthenticationToken principal);
    Product findOneByProductId(String productId);

    List<Product> findAll(int tenantId);
    void createProduct(Product product, UsernamePasswordAuthenticationToken principal);
    boolean deletebyProductId(List<String> listProductId);
    Product findOneByCode(String code);
}
