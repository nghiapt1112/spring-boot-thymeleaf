package com.lyna.web.domain.order.repository.impl;

import com.lyna.commons.infrustructure.repository.BaseRepository;
import com.lyna.web.domain.order.OrderDetail;
import com.lyna.web.domain.order.repository.OrderDetailRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
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
    public boolean deleteByOrderIdsAndTenantId(List<String> orderIds, int tenantId) {
        String query = "DELETE FROM OrderDetail o WHERE o.orderId in (:orderIds) AND o.tenantId=:tenantId";
        entityManager.createQuery(query)
                .setParameter("orderIds", orderIds)
                .setParameter("tenantId", tenantId)
                .executeUpdate();
        return true;
    }


    @Override
    public List<OrderDetail> findByTenantIdAndProductId(int tenantId, String productId) {
        return entityManager.createQuery("SELECT o FROM OrderDetail o WHERE o.tenantId = :tenantId AND o.productId = :productId")
                .setParameter("tenantId", tenantId)
                .setParameter("productId", productId)
                .getResultList();
    }

    @Override
    public List<OrderDetail> findByOrderIdAndProductIdAndTenantId(String orderId, String productId, int tenantId) {
        String query = "SELECT d FROM OrderDetail d JOIN Order t ON t.orderId = d.orderId WHERE t.tenantId = :tenantId AND t.orderId = :orderId " +
                "and d.productId = :productId";
        List list = entityManager.createQuery(query, OrderDetail.class)
                .setParameter("productId", productId)
                .setParameter("tenantId", tenantId)
                .setParameter("orderId", orderId).getResultList();
        if (list != null && list.size() > 0)
            return list;
        else
            return null;
    }

}
