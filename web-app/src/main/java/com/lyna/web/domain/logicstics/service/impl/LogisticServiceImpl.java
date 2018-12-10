package com.lyna.web.domain.logicstics.service.impl;

import com.lyna.commons.infrustructure.exception.DomainException;
import com.lyna.commons.infrustructure.object.RequestPage;
import com.lyna.commons.infrustructure.service.BaseService;
import com.lyna.web.domain.logicstics.LogisticException;
import com.lyna.web.domain.logicstics.LogisticResponsePage;
import com.lyna.web.domain.logicstics.StoreResponsePage;
import com.lyna.web.domain.logicstics.repository.LogisticRepository;
import com.lyna.web.domain.logicstics.service.LogisticService;
import com.lyna.web.domain.mpackage.Package;
import com.lyna.web.domain.mpackage.repository.PackageRepository;
import com.lyna.web.domain.stores.repository.StoreRepository;
import com.lyna.web.domain.view.LogisticAggregate;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.lyna.web.domain.logicstics.Logistics.MAIN_MENU_LOGISTIC_DELIVERY_LIST;
import static com.lyna.web.domain.stores.Store.MAIN_MENU_STORE_ORDER_LIST;

@Service
public class LogisticServiceImpl extends BaseService implements LogisticService {

    @Autowired
    private LogisticRepository logisticRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private PackageRepository packageRepository;

    public LogisticResponsePage findLogisticsAndPaging(RequestPage logisticRequestPage) {
        LogisticResponsePage responses = this.logisticRepository.findWithPaging(logisticRequestPage, LogisticResponsePage.class, MAIN_MENU_LOGISTIC_DELIVERY_LIST);

        if (Objects.isNull(responses)) {
            throw new DomainException("[parse.response.error]");
        }
        List<LogisticAggregate> logisticAggregates = responses.getResults();

        Set<String> uniquePackageIds = new HashSet<>();
        for (LogisticAggregate el: logisticAggregates) {
            uniquePackageIds.add(el.getDeliveryPackageId());
            uniquePackageIds.add(el.getLogisticPackageId());
        }

        List<Package> packagesInTenant = this.packageRepository.findByIds(logisticRequestPage.getTenantId(), uniquePackageIds);

        if (CollectionUtils.isEmpty(packagesInTenant)) {
            throw new LogisticException(toInteger("-1"), toStr("Khong get dc package trong tenant"));
        }
        Map<String, Package> packageById = packagesInTenant.stream().collect(Collectors.toMap(Package::getPakageId, Function.identity()));
        logisticAggregates.stream()
                .peek(el -> el.updatePackage(packageById))
                .collect(Collectors.toList());
        return responses;
    }

    @Override
    public StoreResponsePage findOrdersAndPaging(RequestPage orderRequestPage) {
        StoreResponsePage responses = this.storeRepository.findWithPaging(orderRequestPage, StoreResponsePage.class, MAIN_MENU_STORE_ORDER_LIST);
        if (Objects.isNull(responses)) {
            throw new DomainException("[parse.response.error]");
        }
        return responses;
    }

}
