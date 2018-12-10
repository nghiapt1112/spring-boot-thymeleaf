package com.lyna.web.domain.logicstics.service.impl;

import com.lyna.commons.infrustructure.exception.DomainException;
import com.lyna.commons.infrustructure.object.RequestPage;
import com.lyna.commons.infrustructure.service.BaseService;
import com.lyna.web.domain.delivery.DeliveryDetail;
import com.lyna.web.domain.delivery.repository.DeliveryDetailRepository;
import com.lyna.web.domain.logicstics.LogisticResponsePage;
import com.lyna.web.domain.logicstics.LogiticsDetail;
import com.lyna.web.domain.logicstics.StoreResponsePage;
import com.lyna.web.domain.logicstics.repository.LogisticDetailRepository;
import com.lyna.web.domain.logicstics.repository.LogisticRepository;
import com.lyna.web.domain.logicstics.service.LogisticService;
import com.lyna.web.domain.order.Order;
import com.lyna.web.domain.order.service.OrderService;
import com.lyna.web.domain.stores.repository.StoreRepository;
import com.lyna.web.domain.view.LogisticAggregate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
    private LogisticDetailRepository logisticDetailRepository;

    @Autowired
    private DeliveryDetailRepository deliveryDetailRepository;

    @Autowired
    private OrderService orderService;

    public LogisticResponsePage findLogisticsAndPaging(RequestPage logisticRequestPage) {
        LogisticResponsePage responses = this.logisticRepository.findWithPaging(logisticRequestPage, LogisticResponsePage.class, MAIN_MENU_LOGISTIC_DELIVERY_LIST);

        if (Objects.isNull(responses)) {
            throw new DomainException("[parse.response.error]");
        }
        List<LogisticAggregate> logisticAggregates = responses.getResults();
        Collection<String> orderIds = orderService.findByTenantId(logisticRequestPage.getTenantId())
                .stream()
                .map(Order::getOrderId)
                .collect(Collectors.toSet());

        Map<String, List<LogiticsDetail>> logisticDetailsById = this.logisticDetailRepository
                .findByOrderIds(logisticRequestPage.getTenantId(), orderIds)
                .stream().collect(Collectors.groupingBy(LogiticsDetail::getLogisticsId));


        Map<String, List<DeliveryDetail>> deliveryDetailsById = this.deliveryDetailRepository
                .findByOrderIds(logisticRequestPage.getTenantId(), orderIds)
                .stream().collect(Collectors.groupingBy(DeliveryDetail::getDeliveryId));


        logisticAggregates.stream()
                .peek(el -> el.updatePackage(logisticDetailsById, deliveryDetailsById))
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
