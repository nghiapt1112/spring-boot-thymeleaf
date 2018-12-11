package com.lyna.web.domain.order.repository;

import com.lyna.web.domain.order.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderDetailRepository extends JpaRepository<OrderDetail, String>, CustomOrderDetailRepository{





}
