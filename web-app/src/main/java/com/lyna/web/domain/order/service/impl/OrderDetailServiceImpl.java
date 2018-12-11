package com.lyna.web.domain.order.service.impl;

import com.lyna.commons.infrustructure.service.BaseService;
import com.lyna.web.domain.order.repository.OrderDetailRepository;
import com.lyna.web.domain.order.service.OrderDetailService;
import com.lyna.web.domain.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderDetailServiceImpl extends BaseService implements OrderDetailService {

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private ProductRepository productRepository;

    @Override
    @Transactional
    public boolean deleteByProductIds(List<String> productIds) {
        orderDetailRepository.deleteByProductIds(productIds);
        productRepository.deleteByProductIds(productIds);
        return true;
    }
}
