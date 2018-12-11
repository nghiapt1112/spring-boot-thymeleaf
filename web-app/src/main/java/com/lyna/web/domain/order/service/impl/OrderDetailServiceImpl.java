package com.lyna.web.domain.order.service.impl;

import com.lyna.commons.infrustructure.service.BaseService;
import com.lyna.web.domain.order.repository.OrderDetailRepository;
import com.lyna.web.domain.order.service.OrderDetailService;
import com.lyna.web.domain.product.repository.ProductRepository;
import com.lyna.web.domain.stores.exception.StoreException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderDetailServiceImpl extends BaseService implements OrderDetailService {

    private final Logger log = LoggerFactory.getLogger(OrderDetailServiceImpl.class);

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    @Transactional
    public boolean deleteByProductIdsAndTenantId(List<String> productIds, int tenantId) {
        try {
            orderDetailRepository.deleteByProductIdsAndTenantId(productIds, tenantId);
            productRepository.deleteByProductIdsAndTenantId(productIds, tenantId);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new StoreException(toInteger("err.product.deleteFail.code"), toStr("err.product.deleteFail.msg"));
        }

    }
}
