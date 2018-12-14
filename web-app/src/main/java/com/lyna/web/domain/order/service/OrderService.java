package com.lyna.web.domain.order.service;

import com.lyna.commons.infrustructure.object.RequestPage;
import com.lyna.web.domain.order.Order;
import com.lyna.web.domain.order.OrderAggregate;

import java.util.List;

public interface OrderService {
    List<Order> findByTenantId(int tenantId);

    List<OrderAggregate> findOrderViews(int tenantId, RequestPage requestPage);
}
