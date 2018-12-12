package com.lyna.web.domain.logicstics.service;

import com.lyna.web.domain.view.LogisticAggregate;

import java.util.List;

public interface LogisticService {
    List<LogisticAggregate> findLogisticsView(int tenantId);

}
