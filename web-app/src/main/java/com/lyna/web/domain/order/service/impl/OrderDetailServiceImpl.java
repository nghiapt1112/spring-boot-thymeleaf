package com.lyna.web.domain.order.service.impl;

import com.lyna.commons.infrustructure.service.BaseService;
import com.lyna.web.domain.order.repository.OrderDetailRepository;
import com.lyna.web.domain.order.service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDetailServiceImpl extends BaseService implements OrderDetailService {

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Override
    public boolean deleteByProductId(List<String> productIds) {
        return orderDetailRepository.deleteByProductId(productIds);
    }
}
