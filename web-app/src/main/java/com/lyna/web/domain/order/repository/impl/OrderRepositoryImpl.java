package com.lyna.web.domain.order.repository.impl;

import com.lyna.commons.infrustructure.repository.BaseRepository;
import com.lyna.web.domain.order.Order;
import com.lyna.web.domain.order.OrderDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Transactional
public class OrderRepositoryImpl extends BaseRepository<Order, Long> {

    private final Logger log = LoggerFactory.getLogger(OrderRepositoryImpl.class);

    @PersistenceContext
    private EntityManager em;

    public OrderRepositoryImpl(EntityManager em) {
        super(Order.class, em);
    }

    public void deletebyDate() {
        /*try {
            String query = "SELECT s.orderDate,st.name, s.post,od.amount,   FROM Store s INNER  JOIN  Logistics l ON s.orderId = l.orderId INNER JOIN" +
                    " Store st ON s.storeId = st.storeId INNER JOIN OrderDetail od";
            entityManager.createQuery(query).setParameter("listProductId", listProductId).executeUpdate();
            return true;
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            throw e;
        }*/
    }
}
