package com.lyna.web.domain.order.repository.repositoryImpl;

import com.lyna.commons.infrustructure.repository.BaseRepository;
import com.lyna.web.domain.order.Order;
import com.lyna.web.domain.order.OrderDetail;
import com.lyna.web.domain.order.repository.OrderDetailRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class OrderDetailRepositoryImp extends BaseRepository<OrderDetail, String> implements OrderDetailRepository {

    public OrderDetailRepositoryImp(EntityManager em) {
        super(Order.class, em);
    }
}
