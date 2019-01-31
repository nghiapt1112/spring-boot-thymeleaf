package com.lyna.web.domain.trainningData.service;

import com.lyna.web.domain.trainningData.Training;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface TrainingService {
    void saveMap(Map<String, Map<String, BigDecimal>> mapOrderIdProductIdAmount, Map<String, Map<String, BigDecimal>> mapOrderPackageIdAmount, int tenantId, String userId);

    List<Training> findAllByTenantId(int tenantId);
}
