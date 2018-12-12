package com.lyna.web.domain.delivery.repository;

import com.lyna.web.domain.delivery.DeliveryDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface DeliveryDetailRepository extends JpaRepository<DeliveryDetail, String> {
    List<DeliveryDetail> findByDeliveryIds(int tenantId, Collection<String> deliveryIds);
    List<DeliveryDetail> findByOrderIds(int tenantId, Collection<String> orderIds);
    boolean deleteByPackageIdsAndTenantId(List<String> packageIds, int tenantId);

}
