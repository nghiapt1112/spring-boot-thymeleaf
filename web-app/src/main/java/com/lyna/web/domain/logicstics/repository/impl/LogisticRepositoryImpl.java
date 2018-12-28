package com.lyna.web.domain.logicstics.repository.impl;

import com.lyna.web.domain.logicstics.Logistics;
import com.lyna.web.domain.logicstics.repository.LogisticRepository;
import com.lyna.web.infrastructure.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Set;

@Repository
public class LogisticRepositoryImpl extends BaseRepository<Logistics, String> implements LogisticRepository {

    public LogisticRepositoryImpl(EntityManager em) {
        super(Logistics.class, em);
    }

    @Override
    public List<Logistics> findByOrderIds(int tenantId, Set<String> orderIds) {
        return this.entityManager.createQuery("SELECT lg FROM Logistics AS lg WHERE lg.tenantId = :tenantId AND lg.orderId IN (:orderIds)", Logistics.class)
                .setParameter("tenantId", tenantId)
                .setParameter("orderIds", orderIds)
                .getResultList();
    }
}
