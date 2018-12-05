package com.lyna.web.domain.logicstics.service.impl;

import com.lyna.commons.infrustructure.exception.DomainException;
import com.lyna.commons.infrustructure.object.RequestPage;
import com.lyna.commons.infrustructure.service.BaseService;
import com.lyna.web.domain.logicstics.LogisticQueryBuilder;
import com.lyna.web.domain.logicstics.LogisticResponsePage;
import com.lyna.web.domain.logicstics.StoreQueryBuilder;
import com.lyna.web.domain.logicstics.StoreResponsePage;
import com.lyna.web.domain.logicstics.repository.LogisticRepository;
import com.lyna.web.domain.logicstics.repository.NghiaStoreRepository;
import com.lyna.web.domain.logicstics.service.LogisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * TODO: move queryBuilder.withRequestPage => requestPage.buildQurery().
 */
@Service
@Transactional
public class LogisticServiceImpl extends BaseService implements LogisticService {

    @Autowired
    private LogisticRepository logisticRepository;

    @Autowired
    private NghiaStoreRepository nghiaStoreRepository;

    public LogisticResponsePage findLogisticsAndPaging(RequestPage logisticRequestPage) {
        LogisticResponsePage responses = this.logisticRepository.findWithPaging(logisticRequestPage, new LogisticQueryBuilder().withRequestPage(logisticRequestPage), LogisticResponsePage.class);
        if (Objects.isNull(responses)) {
            throw new DomainException("[parse.response.error]");
        }
        return responses;
    }

    @Override
    public StoreResponsePage findOrdersAndPaging(RequestPage orderRequestPage) {
        StoreResponsePage responses = this.nghiaStoreRepository.findWithPaging(orderRequestPage, new StoreQueryBuilder().withRequestPage(orderRequestPage), StoreResponsePage.class/*, StoreAggregate.class*/);
        if (Objects.isNull(responses)) {
            throw new DomainException("[parse.response.error]");
        }
        return responses;
    }

}
