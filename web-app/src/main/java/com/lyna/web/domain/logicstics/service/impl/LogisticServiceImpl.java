package com.lyna.web.domain.logicstics.service.impl;

import com.lyna.commons.infrustructure.service.BaseService;
import com.lyna.web.domain.logicstics.DeliveryView;
import com.lyna.web.domain.logicstics.LogisticView;
import com.lyna.web.domain.logicstics.repository.LogisticViewRepository;
import com.lyna.web.domain.logicstics.service.LogisticService;
import com.lyna.web.domain.view.LogisticAggregate;
import com.lyna.web.domain.view.PackageAggregate;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LogisticServiceImpl extends BaseService implements LogisticService {


    @Autowired
    private LogisticViewRepository logisticViewRepository;

    @Override
    public List<LogisticAggregate> findLogisticsView(int tenantId) {
        List<LogisticView> logisticView = this.logisticViewRepository.findLogistics(tenantId);
        List<DeliveryView> deliveryView = this.logisticViewRepository.findDeliveries(tenantId);

        Map<String, List<PackageAggregate>> logisticPackagesByOrderId = logisticView.stream()
                .parallel()
                .map(PackageAggregate::fromLogisticView) // to PackageAggregate
                .collect(Collectors.groupingBy(el -> el.getOrderId())); // group packageByOrderId

        Map<String, List<PackageAggregate>> deliveryPackagesByOrderId = deliveryView.stream()
                .parallel()
                .map(PackageAggregate::fromDeliveryView) // to PackageAggregate
                .collect(Collectors.groupingBy(el -> el.getOrderId())); // group packageByOrderId


        List<LogisticAggregate> val = logisticView.stream()
                .parallel()
                .map(el -> LogisticAggregate.parseFromViewDTO(el, logisticPackagesByOrderId, deliveryPackagesByOrderId))
                .collect(Collectors.toList());
        return val;
    }

    @SuppressWarnings("unused")
    private List<String> findOrderPushedToDeliveryAPI(List<LogisticView> logisticView, Map<String, DeliveryView> deliveryViewByOrderId) {
        if (CollectionUtils.isEmpty(logisticView) || MapUtils.isEmpty(deliveryViewByOrderId)) {
            return Collections.EMPTY_LIST;
        }

        return logisticView
                .stream()
                .filter(el -> deliveryViewByOrderId.containsKey(el.getOrderId()))
                .map(LogisticView::getOrderId)
                .collect(Collectors.toList());

    }

}
