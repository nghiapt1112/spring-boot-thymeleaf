package com.lyna.web.domain.order.repository;

import com.lyna.web.domain.order.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    boolean deleteByProductIds(List<String> productIds);

}

