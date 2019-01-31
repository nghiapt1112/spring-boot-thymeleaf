package com.lyna.web.domain.order.service.ServiceImp;


import com.lyna.commons.infrustructure.object.RequestPage;
import com.lyna.commons.infrustructure.service.BaseService;
import com.lyna.web.domain.order.Order;
import com.lyna.web.domain.order.OrderAggregate;
import com.lyna.web.domain.order.OrderView;
import com.lyna.web.domain.order.repository.OrderRepository;
import com.lyna.web.domain.order.service.OrderService;
import com.lyna.web.domain.view.CsvOrder;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Reader;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImp extends BaseService implements OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Override
    public List<Order> findByTenantId(int tenantId) {

        return this.orderRepository.findByTenantId(tenantId);
    }

    @Override
    public List<OrderAggregate> findOrderViews(int tenantId, RequestPage requestPage) {
        List<OrderView> orderViews = this.orderRepository.findOverViews(tenantId, requestPage);
        if (CollectionUtils.isEmpty(orderViews)) {
            return Collections.EMPTY_LIST;
        }
        return orderViews.stream()
                .map(OrderAggregate::parseFromView)
                .collect(Collectors.toList());
    }

    @Override
    public Iterator<CsvOrder> getMapOrder(Reader reader, Map<Integer, String> mapHeader) {
        return orderRepository.getMapOrder(reader, mapHeader);
    }

    @Override
    public Map<String, Integer> getHeaderOrder(Reader reader) {
        return orderRepository.getHeaderOrder(reader);
    }

    @Override
    public List<CSVRecord> getDataOrder(Reader reader) {
        return orderRepository.getDataOrder(reader);
    }

    @Override
    public String getOrderIdByPostCourseIdAndTenantId(String postCourseId, String productId, int tenantId) {
        return orderRepository.getByPostCourseIdOrderDateTenantId(postCourseId, productId, tenantId);
    }

    @Override
    public void saveAll(Set<Order> orderIterable) {
        orderRepository.saveAll(orderIterable);
    }
}
