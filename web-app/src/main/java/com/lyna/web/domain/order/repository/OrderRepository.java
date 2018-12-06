package com.lyna.web.domain.order.repository;

import com.lyna.web.domain.order.Order;
import com.lyna.web.domain.view.CsvOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Reader;
import java.util.Iterator;


public interface OrderRepository extends JpaRepository<Order, String> {

    Iterator<CsvOrder> getMapOrder(Reader targetReader);

    Order save(Order order);

    boolean checkExists(String postCourseId, String productId, String quantity);
}
