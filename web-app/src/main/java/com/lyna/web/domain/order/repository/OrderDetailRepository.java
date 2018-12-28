package com.lyna.web.domain.order.repository;

import com.lyna.web.domain.order.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;


public interface OrderDetailRepository extends JpaRepository<OrderDetail, String> {

    boolean deleteByProductIdsAndTenantId(List<String> productIds, int tenantId);

    boolean deleteByOrderIdsAndTenantId(List<String> orderIds, int tenantId);

    List<OrderDetail> findByTenantIdAndProductId(int tenantId, String productId);

    List<OrderDetail> findByOrderIdAndProductIdAndTenantId(String orderId, String productId, int tenantId);

    List<OrderDetail> findByOrderIds(int tenantId, Collection<String> orderIds);

}
