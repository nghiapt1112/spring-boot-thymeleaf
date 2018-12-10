package com.lyna.web.domain.order.service;

import com.lyna.web.domain.order.Order;

import java.util.List;

public interface OrderService {
    List<Order> findByTenantId(int tenantId);
}
