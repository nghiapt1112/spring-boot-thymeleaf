package com.lyna.web.domain.product.service.impl;

import com.lyna.commons.infrustructure.exception.DomainException;
import com.lyna.commons.infrustructure.service.BaseService;
import com.lyna.web.domain.order.OrderDetail;
import com.lyna.web.domain.order.repository.OrderDetailRepository;
import com.lyna.web.domain.product.Product;
import com.lyna.web.domain.product.exeption.ProductException;
import com.lyna.web.domain.product.repository.ProductRepository;
import com.lyna.web.domain.product.service.ProductService;
import com.lyna.web.domain.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@Service
public class ProductServiceImpl extends BaseService implements ProductService {

    private final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Override
    @Transactional
    public void update(Product product, User user) {
        Date date = new Date();
        product.setUpdateDate(date);
        product.setUpdateUser(user.getId());

        List<OrderDetail> orderDetails = orderDetailRepository.findByTenantIdAndProductId(user.getTenantId(), product.getProductId());
        if(!Objects.isNull(orderDetails)){
            product.setOrderDetails(new HashSet<OrderDetail>(orderDetails));
        }
        try {
            productRepository.save(product);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ProductException(toInteger("err.product.updateError.code"), toStr("err.product.updateError.msg"));
        }

    }

    @Override
    public Product findOneByProductIdAndTenantId(String productId, int tenantId) {
        try {
            return productRepository.findOneByProductIdAndTenantId(productId, tenantId);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ProductException(toInteger("err.product.notFound.code"), toStr("err.product.notFound.msg"));
        }
    }

    @Override
    public List<Product> findByTenantIdAndTenantId(int tenantId) {
        try {
            return productRepository.findByTenantId(tenantId);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ProductException(toInteger("err.product.notFound.code"), toStr("err.product.notFound.msg"));
        }
    }

    @Override
    @Transactional
    public void create(Product product, User user) {
        Date date = new Date();
        product.setCreateDate(date);
        product.setTenantId(user.getTenantId());
        product.setCreateUser(user.getId());
        try {
            productRepository.save(product);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ProductException(toInteger("err.product.createFailed.code"), toStr("err.product.createFailed.msg"));
        }
    }

    @Override
    public Product findOneByCode(String code) {
        try {
            return productRepository.findOneByCode(code);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ProductException(toInteger("err.product.notFound.code"), toStr("err.product.notFound.msg"));
        }

    }
}
