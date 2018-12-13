package com.lyna.web.domain.logicstics.repository;

import java.util.List;

public interface LogiticDetailRepository {
    boolean deleteByPackageIdsAndTenantId(List<String> packageIds, int tenantId);
}
