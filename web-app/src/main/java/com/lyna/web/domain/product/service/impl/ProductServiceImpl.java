package com.lyna.web.domain.product.service.impl;

import com.lyna.commons.infrustructure.exception.DomainException;
import com.lyna.commons.infrustructure.service.BaseService;
import com.lyna.web.domain.product.Product;
import com.lyna.web.domain.product.repository.ProductRepository;
import com.lyna.web.domain.product.service.ProductService;
import com.lyna.web.domain.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class ProductServiceImpl extends BaseService implements ProductService {

    private final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);
    @Autowired
    private ProductRepository productRepository;

    @Override
    public void updateProduct(Product product, UsernamePasswordAuthenticationToken principal) {
        User currentUser = (User) principal.getPrincipal();
        Date date = new Date();
        product.setUpdateDate(date);
        product.setUpdateUser(currentUser.getId());
        product.setTenantId(currentUser.getTenantId());
        productRepository.save(product);
    }

    @Override
    public Product findOneByProductId(String productId) {
        return productRepository.findOneByProductId(productId);
    }

    @Override
    public List<Product> findAll(int tenantId) {
        return productRepository.findAll(tenantId);
    }

    @Override
    @Transactional
    public void createProduct(Product product, UsernamePasswordAuthenticationToken principal) throws DomainException {
        User currentUser = (User) principal.getPrincipal();
        Date date = new Date();

        product.setCreateDate(date);
        product.setTenantId(currentUser.getTenantId());
        product.setCreateUser(currentUser.getId());
        try {
            productRepository.save(product);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }

    @Override
    public boolean deletebyProductId(List<String> listProductId) {
        return productRepository.deletebyProductId(listProductId);
    }

    @Override
    public Product findOneByCode(String code) {
        return productRepository.findOneByCode(code);
    }
}
