package com.lyna.web.domain.delivery.repository.impl;

import com.lyna.commons.infrustructure.repository.BaseRepository;
import com.lyna.web.domain.delivery.DeliveryDetail;
import com.lyna.web.domain.delivery.repository.DeliveryDetailRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class DeliveryDetailRepositoryImpl extends BaseRepository<DeliveryDetail, Long> implements DeliveryDetailRepository {

    public DeliveryDetailRepositoryImpl(EntityManager em) {
        super(DeliveryDetail.class, em);
    }

    @Override
    public String checkExistByDeliveryId(String deliveryId, String packageName, int tenantId) {
        List list = entityManager
                .createQuery("SELECT deliveryDetailId from  DeliveryDetail d join Package p on d.packageId = p.packageId " +
                        " WHERE d.deliveryId = :deliveryId and d.tenantId = :tenantId  and p.name =  :packageName")
                .setParameter("deliveryId", deliveryId)
                .setParameter("tenantId", tenantId)
                .setParameter("packageName", packageName)
                .getResultList();
        if (list != null && list.size() > 0)
            return (String) list.get(0);
        return null;
    }
}
