package com.lyna.web.domain.logicstics.service;

import java.util.Map;

public interface LogisticService {
     static final String LOGISTIC_DATA = "logisticData";
     static final String PKG_TYPE = "packageTypes";

    Map<String, Object> findLogisticsView(int tenantId);

}
