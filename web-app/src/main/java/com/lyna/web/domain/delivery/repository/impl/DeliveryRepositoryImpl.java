package com.lyna.web.domain.delivery.repository.impl;

import com.lyna.commons.infrustructure.repository.BaseRepository;
import com.lyna.web.domain.delivery.Delivery;
import com.lyna.web.domain.delivery.DeliveryDetail;
import com.lyna.web.domain.delivery.repository.DeliveryDetailRepository;
import com.lyna.web.domain.delivery.repository.DeliveryRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class DeliveryRepositoryImpl extends BaseRepository<Delivery, Long> implements DeliveryRepository {

    public DeliveryRepositoryImpl(EntityManager em) {
        super(DeliveryDetail.class, em);
    }

    @Override
    public String checkExistByOrderIdAndOrderDate(String orderId, String orderDate) {
        List list = entityManager
                .createQuery("SELECT d.orderId FROM Delivery d WHERE d.orderId = :orderId")
                .setParameter("orderId", orderId)
                .getResultList();
        if (list != null && list.size() > 0)
            return (String) list.get(0);

        return null;
    }
}
