package com.lyna.web.domain.delivery.repository;

import com.lyna.web.domain.delivery.DeliveryDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeliveryDetailRepository extends JpaRepository<DeliveryDetail, Long> {
    boolean deleteByPackageIdsAndTenantId(List<String> packageIds, int tenantId);
}
