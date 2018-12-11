package com.lyna.web.domain.order.service.ServiceImp;


import com.lyna.commons.infrustructure.service.BaseService;
import com.lyna.web.domain.order.Order;
import com.lyna.web.domain.order.repository.OrderRepository;
import com.lyna.web.domain.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImp extends BaseService implements OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Override
    public List<Order> findByTenantId(int tenantId) {

        return this.orderRepository.findByTenantId(tenantId);
    }
}
