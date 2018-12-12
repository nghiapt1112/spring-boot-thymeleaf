package com.lyna.web.domain.logicstics.repository.impl;

import com.lyna.web.domain.logicstics.DeliveryView;
import com.lyna.web.domain.logicstics.LogisticView;
import com.lyna.web.domain.logicstics.repository.LogisticViewRepository;
import com.lyna.web.infrastructure.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class LogisticVewRepositoryImpl extends BaseRepository<LogisticView, String> implements LogisticViewRepository {

    public LogisticVewRepositoryImpl(EntityManager em) {
        super(LogisticView.class, em);
    }

    @Override
    public List<LogisticView> findLogistics(int tenantId) {
        return entityManager.createQuery("SELECT v FROM LogisticView v", LogisticView.class)
                .getResultList();
    }

    @Override
    public List<DeliveryView> findDeliveries(int tenantId) {
        return entityManager.createQuery("SELECT v FROM DeliveryView v", DeliveryView.class)
                .getResultList();
    }

}
