package com.lyna.web.domain.order.repository;

import com.lyna.web.domain.order.Order;
import com.lyna.web.domain.order.OrderView;
import com.lyna.web.domain.view.CsvOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Reader;
import java.util.Iterator;
import java.util.List;


public interface OrderRepository extends JpaRepository<Order, String> {

    Iterator<CsvOrder> getMapOrder(Reader targetReader);

    Order save(Order order);

    boolean checkExists(String postCourseId, String productId, String quantity);

    List<Order> findByTenantId(int tenantId);

    List<OrderView> findOverViews(int tenantId);

    String checkExists(String postcourseId, String orderDate);
}
