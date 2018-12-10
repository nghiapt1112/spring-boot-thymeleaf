package com.lyna.web.domain.order.repository.impl;

import com.lyna.commons.infrustructure.repository.BaseRepository;
import com.lyna.web.domain.order.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Repository
@Transactional
public class OrderRepositoryImpl extends BaseRepository<Order, Long> {

    private final Logger log = LoggerFactory.getLogger(OrderRepositoryImpl.class);


    public OrderRepositoryImpl(EntityManager em) {
        super(Order.class, em);
    }


}
