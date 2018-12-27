package com.lyna.web.domain.logicstics.repository;

import com.lyna.web.domain.logicstics.Logistics;
import com.lyna.web.infrastructure.repository.PagingRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LogisticRepository extends JpaRepository<Logistics, String>, PagingRepository {
    List<Logistics> findByTenantIdAndOrderId(int tenantId, String orderId);
}
