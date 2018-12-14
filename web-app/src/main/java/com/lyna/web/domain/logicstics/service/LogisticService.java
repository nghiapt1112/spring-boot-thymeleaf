package com.lyna.web.domain.logicstics.service;

import com.lyna.commons.infrustructure.object.RequestPage;

import java.util.Map;

public interface LogisticService {
    String LOGISTIC_DATA = "logisticData";
    String PKG_TYPE = "packageTypes";

    Map<String, Object> findLogisticsView(int tenantId, RequestPage logisticRequestPage);

}
