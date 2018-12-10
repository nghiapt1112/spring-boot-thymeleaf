package com.lyna.web.domain.delivery.repository.impl;

import com.lyna.web.domain.delivery.DeliveryDetail;
import com.lyna.web.domain.delivery.repository.DeliveryDetailRepository;
import com.lyna.web.domain.logicstics.LogiticsDetail;
import com.lyna.web.infrastructure.repository.BaseRepository;
import com.lyna.web.infrastructure.repository.PagingRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Collection;
import java.util.List;

@Repository
public class DeliveryDetailRepositoryImpl extends BaseRepository<DeliveryDetail, String> implements DeliveryDetailRepository, PagingRepository {

    public DeliveryDetailRepositoryImpl(EntityManager em) {
        super(DeliveryDetail.class, em);
    }

    @Override
    public List<DeliveryDetail> findByDeliveryIds(int tenantId, Collection<String> deliveryIds) {
        return this.entityManager.createQuery("SELECT dldt FROM DeliveryDetail dldt left join fetch dldt.pack WHERE dldt.tenantId = :tenantId AND dldt.deliveryId IN (:deliveryIds)", DeliveryDetail.class)
                .setParameter("tenantId", tenantId)
                .setParameter("deliveryIds", deliveryIds)
                .getResultList();
    }

    @Override
    public List<DeliveryDetail> findByOrderIds(int tenantId, Collection<String> orderIds) {
        return this.entityManager.createQuery("SELECT dldt FROM DeliveryDetail dldt left join fetch dldt.pack " +
                "WHERE dldt.tenantId = :tenantId " +
                "AND dldt.deliveryId IN (SELECT dlvr.id FROM Delivery dlvr WHERE dlvr.orderId IN (:orderIds))", DeliveryDetail.class)
                .setParameter("tenantId", tenantId)
                .setParameter("orderIds", orderIds)
                .getResultList();
    }
}
