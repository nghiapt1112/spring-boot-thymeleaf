package com.lyna.web.domain.product.service;

import com.lyna.web.domain.product.Product;
import com.lyna.web.domain.user.User;

import java.util.List;

public interface ProductService {
    void update(Product product, User user);

    Product findOneByProductIdAndTenantId(String productId, int tenantId);

    List<Product> findByTenantIdAndTenantId(int tenantId);

    void create(Product product, User user);

    Product findOneByCodeAndTenantId(String code, int tenantId);

}
