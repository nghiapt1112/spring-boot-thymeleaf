package com.lyna.web.domain.logicstics.repository.impl;

import com.lyna.web.domain.logicstics.Logistics;
import com.lyna.web.domain.logicstics.repository.LogisticRepository;
import com.lyna.web.infrastructure.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class LogisticRepositoryImpl extends BaseRepository<Logistics, String> implements LogisticRepository {

    public LogisticRepositoryImpl(EntityManager em) {
        super(Logistics.class, em);
    }

    @Override
    public List<Logistics> findByTenantIdAndOrderId(int tenantId, String orderId) {
        return entityManager.createQuery("SELECT l FROM Logistics l WHERE l.tenantId = :tenantId AND l.orderId = :orderId")
                .setParameter("tenantId", tenantId)
                .setParameter("orderId", orderId)
                .getResultList();
    }

}
