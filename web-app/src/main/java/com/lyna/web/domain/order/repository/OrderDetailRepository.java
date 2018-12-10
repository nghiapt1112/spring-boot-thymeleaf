package com.lyna.web.domain.order.repository;

import com.lyna.web.domain.order.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


public interface OrderDetailRepository extends JpaRepository<OrderDetail, String> {

}
