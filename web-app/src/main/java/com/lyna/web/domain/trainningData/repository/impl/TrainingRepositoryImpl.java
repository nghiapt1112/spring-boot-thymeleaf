package com.lyna.web.domain.trainningData.repository.impl;

import com.lyna.web.domain.trainningData.Training;
import com.lyna.web.domain.trainningData.repository.TrainingRepository;
import com.lyna.web.infrastructure.repository.BaseRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@Transactional
public class TrainingRepositoryImpl extends BaseRepository<Training, String> implements TrainingRepository {

    public TrainingRepositoryImpl(EntityManager em) {
        super(Training.class, em);
    }

    @Override
    public List<Training> findByOrderIdTenantId(List<String> orderIds, int tenantId) {
        return entityManager
                .createQuery("SELECT s FROM Training s WHERE s.tenantId=:tenantId and s.orderId in (:orderIds) order by s.createDate ", Training.class)
                .setParameter("tenantId", tenantId)
                .setParameter("orderIds", orderIds)
                .getResultList();
    }

    @Override
    public List<Training> findByTenantId(int tenantId) {
        return entityManager
                .createQuery("SELECT s FROM Training s WHERE s.tenantId=:tenantId order by s.createDate ", Training.class)
                .setParameter("tenantId", tenantId)
                .getResultList();
    }
}
