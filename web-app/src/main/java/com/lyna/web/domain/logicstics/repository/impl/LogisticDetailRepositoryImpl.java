package com.lyna.web.domain.logicstics.repository.impl;

import com.lyna.commons.infrustructure.exception.DomainException;
import com.lyna.web.domain.logicstics.LogiticsDetail;
import com.lyna.web.domain.logicstics.repository.LogisticDetailRepository;
import com.lyna.web.infrastructure.repository.BaseRepository;

import javax.persistence.EntityManager;
import java.util.Collection;
import java.util.List;

public class LogisticDetailRepositoryImpl extends BaseRepository<LogiticsDetail, String> implements LogisticDetailRepository {

    public LogisticDetailRepositoryImpl(EntityManager em) {
        super(LogiticsDetail.class, em);
    }

    @Override
    public List<LogiticsDetail> findByLogisticIds(int tenantId, Collection<String> logisticIds) {
        return super.entityManager.createQuery("SELECT lgdt FROM LogiticsDetail lgdt left join fetch lgdt.pack WHERE lgdt.tenantId = :tenantId AND lgdt.logisticsId IN (:logisticIds)", LogiticsDetail.class)
                .setParameter("tenantId", tenantId)
                .setParameter("logisticIds", logisticIds)
                .getResultList();
    }

    @Override
    public List<LogiticsDetail> findByOrderIds(int tenantId, Collection<String> orderIds) {
        return super.entityManager.createQuery("SELECT lgdt FROM LogiticsDetail lgdt left join fetch lgdt.pack " +
                "WHERE lgdt.tenantId = :tenantId " +
                "AND lgdt.logisticsId IN (SELECT lgst.id FROM Logistics lgst WHERE lgst.orderId IN (:orderIds))", LogiticsDetail.class)
                .setParameter("tenantId", tenantId)
                .setParameter("orderIds", orderIds)
                .getResultList();
    }

    @Override
    public List<LogiticsDetail> findByTenantIdAndPackageId(int tenantId, String packageId) {
        return entityManager.createQuery("SELECT l FROM LogiticsDetail l WHERE l.tenantId = :tenantId AND l.packageId = :packageId")
                .setParameter("tenantId", tenantId)
                .setParameter("packageId", packageId)
                .getResultList();
    }

    @Override
    public List<LogiticsDetail> findByTenantIdAndLogisticsId(int tenantId, String logisticsId) {
        return entityManager.createQuery("SELECT l FROM LogiticsDetail l WHERE l.tenantId = :tenantId AND l.logisticsId = :logisticsId")
                .setParameter("tenantId", tenantId)
                .setParameter("logisticsId", logisticsId)
                .getResultList();
    }

    @Override
    public boolean deleteByPackageIdsAndTenantId(List<String> packageIds, int tenantId) throws DomainException {
        String query = "DELETE FROM LogiticsDetail l WHERE l.packageId in (:packageIds) AND l.tenantId=:tenantId";
        entityManager.createQuery(query)
                .setParameter("packageIds", packageIds)
                .setParameter("tenantId", tenantId)
                .executeUpdate();
        return true;
    }

    @Override
    public boolean deleteByLogisticsIdAndTenantId(List<String> logisticsIds, int tenantId) {
        String query = "DELETE FROM LogiticsDetail l WHERE l.logisticsId in (:logisticsIds) AND l.tenantId=:tenantId";
        entityManager.createQuery(query)
                .setParameter("logisticsIds", logisticsIds)
                .setParameter("tenantId", tenantId)
                .executeUpdate();
        return true;
    }

    @Override
    public List<LogiticsDetail> findByTenantId(int tenantId) {
        return super.entityManager.createQuery("SELECT l FROM LogiticsDetail l WHERE l.tenantId = :tenantId")
                .setParameter("tenantId", tenantId)
                .getResultList();
    }
}
