package com.lyna.web.domain.order.repository.impl;

import com.lyna.commons.infrustructure.repository.BaseRepository;
import com.lyna.commons.infrustructure.service.BaseService;
import com.lyna.web.domain.order.OrderDetail;
import com.lyna.web.domain.order.repository.OrderDetailRepository;
import com.lyna.web.domain.product.Product;
import com.lyna.web.domain.stores.repository.impl.StoreRepositoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Transactional
public class OrderDetailRepositoryImpl extends BaseRepository<OrderDetail, Long>  implements OrderDetailRepository {

    private final Logger log = LoggerFactory.getLogger(OrderDetailRepositoryImpl.class);

    @PersistenceContext
    private EntityManager em;

    public OrderDetailRepositoryImpl(EntityManager em) {
        super(OrderDetail.class, em);
    }

    @Override
    public boolean deletebyProductId(List<String> listProductId) {
        try {
            String query = "DELETE FROM OrderDetail o WHERE o.productId in (:listProductId)";
            entityManager.createQuery(query).setParameter("listProductId", listProductId).executeUpdate();
            return true;
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            throw e;
        }
    }
}
