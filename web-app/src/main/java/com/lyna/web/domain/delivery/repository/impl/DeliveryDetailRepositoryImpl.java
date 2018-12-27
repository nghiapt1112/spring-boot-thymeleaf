package com.lyna.web.domain.delivery.repository.impl;

import com.lyna.web.domain.delivery.DeliveryDetail;
import com.lyna.web.domain.delivery.repository.DeliveryDetailRepository;
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

    @Override
    public boolean deleteDeliveryDetailByPackageIdsAndTenantId(List<String> packageIds, int tenantId) {
        String query = "DELETE FROM DeliveryDetail d WHERE d.packageId in (:packageIds) AND d.tenantId=:tenantId";
        entityManager.createQuery(query)
                .setParameter("packageIds", packageIds)
                .setParameter("tenantId", tenantId)
                .executeUpdate();
        return true;

    }

    public String checkExistByDeliveryId(String deliveryId, String packageId, int tenantId) {
        List list = entityManager
                .createQuery("SELECT deliveryDetailId from  DeliveryDetail d  WHERE d.deliveryId = :deliveryId and d.tenantId = :tenantId  and d.packageId =  :packageId")
                .setParameter("deliveryId", deliveryId)
                .setParameter("tenantId", tenantId)
                .setParameter("packageId", packageId)
                .getResultList();
        if (list != null && list.size() > 0)
            return (String) list.get(0);
        return null;
    }

    @Override
    public List<DeliveryDetail> findByTenantIdAndPackageId(int tenantId, String packageId) {
        return entityManager.createQuery("SELECT d FROM DeliveryDetail d WHERE d.tenantId = :tenantId AND d.packageId = :packageId")
                .setParameter("tenantId", tenantId)
                .setParameter("packageId", packageId)
                .getResultList();
    }

}
