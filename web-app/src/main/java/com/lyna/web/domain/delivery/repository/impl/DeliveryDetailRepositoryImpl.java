package com.lyna.web.domain.delivery.repository.impl;

import com.lyna.web.domain.delivery.DeliveryDetail;
import com.lyna.web.domain.delivery.repository.DeliveryDetailRepository;
import com.lyna.web.infrastructure.repository.BaseRepository;
import com.lyna.web.infrastructure.repository.PagingRepository;
import com.lyna.commons.infrustructure.exception.DomainException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Collection;
import java.util.List;

@Repository
public class DeliveryDetailRepositoryImpl extends BaseRepository<DeliveryDetail, String> implements DeliveryDetailRepository, PagingRepository {

    private final Logger log = LoggerFactory.getLogger(DeliveryDetailRepositoryImpl.class);

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

    public boolean deleteByPackageIdsAndTenantId(List<String> packageIds, int tenantId) throws DomainException {
        try {
            String query = "DELETE FROM DeliveryDetail d WHERE d.packageId in (:packageIds) AND d.tenantId=:tenantId";
            entityManager.createQuery(query)
                    .setParameter("packageIds", packageIds)
                    .setParameter("tenantId", tenantId)
                    .executeUpdate();
            return true;
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return false;
        }
    }

}
