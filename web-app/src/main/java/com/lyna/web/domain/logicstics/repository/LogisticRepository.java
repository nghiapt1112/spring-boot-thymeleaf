package com.lyna.web.domain.logicstics.repository;

import com.lyna.web.domain.logicstics.Logistics;
import com.lyna.web.infrastructure.repository.PagingRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface LogisticRepository extends JpaRepository<Logistics, String>, PagingRepository {
    List<String> findByTenantIdAndOrderId(int tenantId, String orderId);

    List<String> findByTenantIdAndOrderIds(int tenantId, List<String> orderIds);

    void deleteByLogisticsIdsAndTenantId(List<String> logisticsIds, int tenantId);

    List<Logistics> findByOrderIds(int tenantId, Collection<String> orderIds);
}