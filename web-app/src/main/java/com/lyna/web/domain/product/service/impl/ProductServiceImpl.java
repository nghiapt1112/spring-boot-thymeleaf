package com.lyna.web.domain.product.service.impl;

import com.lyna.commons.infrustructure.exception.DomainException;
import com.lyna.commons.infrustructure.service.BaseService;
import com.lyna.web.domain.product.Product;
import com.lyna.web.domain.product.repository.ProductRepository;
import com.lyna.web.domain.product.service.ProductService;
import com.lyna.web.domain.stores.exception.StoreException;
import com.lyna.web.domain.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    public void update(Product product, User user) {
        Date date = new Date();
        product.setUpdateDate(date);
        product.setUpdateUser(user.getId());
        product.setTenantId(user.getTenantId());

        try {
            productRepository.save(product);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new StoreException(toInteger("err.product.updateError.code"), toStr("err.product.updateError.msg"));
        }

    }

    @Override
    public Product findOneByProductIdAndTenantId(String productId, int tenantId) {
        try {
            return productRepository.findOneByProductIdAndTenantId(productId, tenantId);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new StoreException(toInteger("err.product.notFound.code"), toStr("err.product.notFound.msg"));
        }
    }

    @Override
    public List<Product> findByTenantIdAndTenantId(int tenantId) {
        try {
            return productRepository.findByTenantId(tenantId);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new StoreException(toInteger("err.product.notFound.code"), toStr("err.product.notFound.msg"));
        }
    }

    @Override
    @Transactional
    public void create(Product product, User user) throws DomainException {
        Date date = new Date();
        product.setCreateDate(date);
        product.setTenantId(user.getTenantId());
        product.setCreateUser(user.getId());
        try {
            productRepository.save(product);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new StoreException(toInteger("err.product.saveError.code"), toStr("err.product.saveError.msg"));
        }
    }

    @Override
    public Product findOneByCodeAndTenantId(String code, int tenantId) {
        try {
            return productRepository.findOneByCodeAndTenantId(code, tenantId);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new StoreException(toInteger("err.product.notFound.code"), toStr("err.product.notFound.msg"));
        }

    }
}
