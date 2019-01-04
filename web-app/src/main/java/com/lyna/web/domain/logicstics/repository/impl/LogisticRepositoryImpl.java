package com.lyna.web.domain.logicstics.repository.impl;

import com.lyna.web.domain.logicstics.Logistics;
import com.lyna.web.domain.logicstics.repository.LogisticRepository;
import com.lyna.web.infrastructure.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Repository
public class LogisticRepositoryImpl extends BaseRepository<Logistics, String> implements LogisticRepository {

    public LogisticRepositoryImpl(EntityManager em) {
        super(Logistics.class, em);
    }

    @Override
    public List<String> findByTenantIdAndOrderId(int tenantId, String orderId) {
        return entityManager.createQuery("SELECT l FROM Logistics l WHERE l.tenantId = :tenantId AND l.orderId = :orderId")
                .setParameter("tenantId", tenantId)
                .setParameter("orderId", orderId)
                .getResultList();
    }

    @Override
    public List<String> findByTenantIdAndOrderIds(int tenantId, List<String> orderIds) {
        return entityManager.createQuery("SELECT l.logisticsId FROM Logistics l WHERE l.tenantId = :tenantId AND l.orderId in (:orderIds)")
                .setParameter("tenantId", tenantId)
                .setParameter("orderIds", orderIds)
                .getResultList();
    }

    @Override
    public void deleteByLogisticsIdsAndTenantId(List<String> logisticsIds, int tenantId) {
        String query = "DELETE FROM Logistics l WHERE l.logisticsId in (:logisticsIds) AND l.tenantId=:tenantId";
        entityManager.createQuery(query)
                .setParameter("logisticsIds", logisticsIds)
                .setParameter("tenantId", tenantId)
                .executeUpdate();
    }

    @Override
    public List<Logistics> findByOrderIds(int tenantId, Collection<String> orderIds) {
        return this.entityManager.createQuery("SELECT lg FROM Logistics AS lg left join fetch lg.logiticsDetails WHERE lg.tenantId = :tenantId AND lg.orderId IN (:orderIds)", Logistics.class)
                .setParameter("tenantId", tenantId)
                .setParameter("orderIds", orderIds)
                .getResultList();
    }

}
