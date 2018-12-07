package com.lyna.web.domain.logicstics.service.impl;

import com.lyna.commons.infrustructure.exception.DomainException;
import com.lyna.commons.infrustructure.object.RequestPage;
import com.lyna.commons.infrustructure.service.BaseService;
import com.lyna.web.domain.logicstics.LogisticResponsePage;
import com.lyna.web.domain.logicstics.StoreResponsePage;
import com.lyna.web.domain.logicstics.repository.LogisticRepository;
import com.lyna.web.domain.logicstics.repository.NghiaStoreRepository;
import com.lyna.web.domain.logicstics.service.LogisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.lyna.web.domain.logicstics.Logistics.MAIN_MENU_LOGISTIC_DELIVERY_LIST;
import static com.lyna.web.domain.stores.Store.MAIN_MENU_STORE_ORDER_LIST;

@Service
public class LogisticServiceImpl extends BaseService implements LogisticService {

    @Autowired
    private LogisticRepository logisticRepository;

    @Autowired
    private NghiaStoreRepository nghiaStoreRepository;

    public LogisticResponsePage findLogisticsAndPaging(RequestPage logisticRequestPage) {
        LogisticResponsePage responses = this.logisticRepository.findWithPaging(logisticRequestPage, LogisticResponsePage.class, MAIN_MENU_LOGISTIC_DELIVERY_LIST);

        if (Objects.isNull(responses)) {
            throw new DomainException("[parse.response.error]");
        }
        return responses;
    }

    @Override
    public StoreResponsePage findOrdersAndPaging(RequestPage orderRequestPage) {
        StoreResponsePage responses = this.nghiaStoreRepository.findWithPaging(orderRequestPage, StoreResponsePage.class, MAIN_MENU_STORE_ORDER_LIST);
        if (Objects.isNull(responses)) {
            throw new DomainException("[parse.response.error]");
        }
        return responses;
    }

}
