package com.lyna.web.domain.logicstics.repository;

import com.lyna.web.domain.logicstics.DeliveryView;
import com.lyna.web.domain.logicstics.LogisticView;

import java.util.List;

public interface LogisticViewRepository {
    List<LogisticView> findLogistics(int tenantId);

    List<DeliveryView> findDeliveries(int tenantId);

}
