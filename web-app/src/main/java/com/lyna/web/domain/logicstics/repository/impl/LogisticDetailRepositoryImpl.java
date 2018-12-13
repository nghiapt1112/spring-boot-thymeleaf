package com.lyna.web.domain.logicstics.repository.impl;

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
}
