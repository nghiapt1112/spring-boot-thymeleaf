package com.lyna.web.domain.logicstics.service;

import com.lyna.commons.infrustructure.object.RequestPage;
import com.lyna.web.domain.logicstics.LogisticResponsePage;
import com.lyna.web.domain.logicstics.StoreResponsePage;

public interface LogisticService {
    LogisticResponsePage findLogisticsAndPaging(RequestPage requestPage);
    StoreResponsePage findOrdersAndPaging(RequestPage requestPage);
}
