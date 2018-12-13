package com.lyna.web.domain.delivery.repository;

import com.lyna.web.domain.delivery.DeliveryDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryDetailRepository extends JpaRepository<DeliveryDetail, Long> {
    String checkExistByDeliveryId(String deliveryId, String packageId, int tenantId);
}
