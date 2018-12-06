package com.lyna.web.domain.product.service;

import com.lyna.web.domain.mpackage.Package;
import com.lyna.web.domain.product.Product;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.List;

public interface ProductService {
    void updateProduct(Product product, UsernamePasswordAuthenticationToken principal);
    Product findOneByProductId(String productId);
    List<Product> findAll();
    void createProduct(Product product, UsernamePasswordAuthenticationToken principal);
    boolean deletebyProductId(List<String> listProductId);
}
