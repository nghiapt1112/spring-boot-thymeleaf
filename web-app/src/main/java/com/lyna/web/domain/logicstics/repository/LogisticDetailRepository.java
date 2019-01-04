package com.lyna.web.domain.logicstics.repository;

import com.lyna.web.domain.logicstics.LogiticsDetail;
import com.lyna.web.infrastructure.repository.PagingRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface LogisticDetailRepository extends JpaRepository<LogiticsDetail, String>, PagingRepository {
    List<LogiticsDetail> findByTenantId(int tenantId);

    List<LogiticsDetail> findByLogisticIds(int tenantId, Collection<String> logisticIds);

    List<LogiticsDetail> findByOrderIds(int tenantId, Collection<String> orderIds);

    List<LogiticsDetail> findByTenantIdAndLogisticsId(int tenantId, String logisticsId);

    boolean deleteByPackageIdsAndTenantId(List<String> packageIds, int tenantId);

    boolean deleteByLogisticsIdAndTenantId(List<String> logisticsIds, int tenantId);

    List<LogiticsDetail> findByTenantIdAndPackageId(int tenantId, String packageId);

}
