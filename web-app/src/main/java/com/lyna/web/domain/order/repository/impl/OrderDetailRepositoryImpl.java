package com.lyna.web.domain.order.repository.impl;

import com.lyna.commons.infrustructure.repository.BaseRepository;
import com.lyna.web.domain.order.OrderDetail;
import com.lyna.web.domain.order.repository.OrderDetailRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class OrderDetailRepositoryImpl extends BaseRepository<OrderDetail, String> implements OrderDetailRepository {

    private final Logger log = LoggerFactory.getLogger(OrderDetailRepositoryImpl.class);

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

}
