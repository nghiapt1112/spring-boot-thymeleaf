package com.lyna.web.domain.order.service.impl;

import com.lyna.commons.infrustructure.repository.BaseRepository;
import com.lyna.commons.infrustructure.service.BaseService;
import com.lyna.web.domain.order.repository.OrderDetailRepository;
import com.lyna.web.domain.order.service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;

@Service
public class OrderDetailServiceImpl extends BaseService implements OrderDetailService {

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Override
    public boolean deletebyProductId(List<String> listProductId) {
        return orderDetailRepository.deletebyProductId(listProductId);
    }
}
