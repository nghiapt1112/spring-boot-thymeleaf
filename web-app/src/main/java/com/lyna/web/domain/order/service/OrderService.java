package com.lyna.web.domain.order.service;

import com.lyna.commons.infrustructure.object.RequestPage;
import com.lyna.web.domain.order.Order;
import com.lyna.web.domain.order.OrderAggregate;
import com.lyna.web.domain.view.CsvOrder;
import org.apache.commons.csv.CSVRecord;

import java.io.Reader;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface OrderService {
    List<Order> findByTenantId(int tenantId);

    List<OrderAggregate> findOrderViews(int tenantId, RequestPage requestPage);

    Iterator<CsvOrder> getMapOrder(Reader reader, Map<Integer, String> mapHeader);

    Map<String, Integer> getHeaderOrder(Reader reader);

    List<CSVRecord> getDataOrder(Reader reader);

    String getOrderIdByPostCourseIdAndTenantId(String postCourseId, String productId, int tenantId);

    void saveAll(Set<Order> orderIterable);
}
