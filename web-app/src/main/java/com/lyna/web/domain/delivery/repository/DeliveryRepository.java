package com.lyna.web.domain.delivery.repository;

import com.lyna.web.domain.delivery.Delivery;
import com.lyna.web.domain.delivery.DeliveryDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    String checkExistByOrderIdAndOrderDate(String orderId, String orderDate);
}
