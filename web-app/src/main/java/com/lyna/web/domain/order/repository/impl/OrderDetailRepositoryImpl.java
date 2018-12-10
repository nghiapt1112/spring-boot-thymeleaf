package com.lyna.web.domain.order.repository.impl;

import com.lyna.commons.infrustructure.exception.DomainException;
import com.lyna.commons.infrustructure.repository.BaseRepository;
import com.lyna.web.domain.order.OrderDetail;
import com.lyna.web.domain.order.repository.OrderDetailRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@Transactional
public class OrderDetailRepositoryImpl extends BaseRepository<OrderDetail, Long> implements OrderDetailRepository {

    private final Logger log = LoggerFactory.getLogger(OrderDetailRepositoryImpl.class);

    public OrderDetailRepositoryImpl(EntityManager em) {
        super(OrderDetail.class, em);
    }

    @Override
    public boolean deleteByProductIds(List<String> productIds) throws DomainException {
        try {
            String query = "DELETE FROM OrderDetail o WHERE o.productId in (:productIds)";
            entityManager.createQuery(query).setParameter("productIds", productIds).executeUpdate();
            return true;
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return false;
        }
    }
}
