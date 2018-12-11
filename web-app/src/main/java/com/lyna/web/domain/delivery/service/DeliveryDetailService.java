package com.lyna.web.domain.delivery.service;

import java.util.List;

public interface DeliveryDetailService {
    boolean deleteByPackageIdsAndTenantId(List<String> packageIds, int tenantId);
}
