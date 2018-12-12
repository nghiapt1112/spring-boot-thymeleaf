package com.lyna.web.domain.order.service;

import java.util.List;

public interface OrderDetailService {
    boolean deleteByProductIdsAndTenantId(List<String> productIds, int tenantId);
}
