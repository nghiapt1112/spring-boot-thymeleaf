package com.lyna.web.domain.delivery.repository;

import com.lyna.web.domain.delivery.Delivery;
import com.lyna.web.domain.view.CsvDelivery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Reader;
import java.util.Iterator;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    String checkExistByOrderIdAndOrderDate(String orderId, String orderDate);

    Iterator<CsvDelivery> getMapDelivery(Reader reader);
}
