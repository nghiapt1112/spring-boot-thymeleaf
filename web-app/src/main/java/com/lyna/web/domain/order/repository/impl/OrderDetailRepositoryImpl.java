package com.lyna.web.domain.order.repository.impl;

import com.lyna.web.domain.order.OrderDetail;
import com.lyna.web.domain.order.repository.OrderDetailRepository;
import com.lyna.web.infrastructure.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Collection;
import java.util.List;

@Repository
public class OrderDetailRepositoryImpl extends BaseRepository<OrderDetail, String> implements OrderDetailRepository {

    public OrderDetailRepositoryImpl(EntityManager em) {
        super(OrderDetail.class, em);
    }

    @Override
    public boolean deleteByProductIdsAndTenantId(List<String> productIds, int tenantId) {
        String query = "DELETE FROM OrderDetail o WHERE o.productId in (:productIds) AND o.tenantId=:tenantId";
        entityManager.createQuery(query)
                .setParameter("productIds", productIds)
                .setParameter("tenantId", tenantId)
                .executeUpdate();
        return true;
    }

    @Override
    public List<OrderDetail> findByOrderIds(int tenantId, Collection<String> orderIds) {
        String query = "SELECT o FROM OrderDetail o WHERE o.tenantId=:tenantId AND o.orderId in (:orderIds)";
        return entityManager.createQuery(query)
                .setParameter("tenantId", tenantId)
                .setParameter("orderIds", orderIds)
                .getResultList();
    }
}
