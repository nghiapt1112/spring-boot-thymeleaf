package com.lyna.web.domain.order.repository;

import com.lyna.web.domain.order.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface OrderDetailRepository extends JpaRepository<OrderDetail, String> {

    boolean deleteByProductIdsAndTenantId(List<String> productIds, int tenantId);

    List<OrderDetail> findByTenantIdAndProductId(int tenantId, String productId);

    List<OrderDetail> findByOrderIdAndProductIdAndTenantId(String orderId, String productId, int tenantId);


}
