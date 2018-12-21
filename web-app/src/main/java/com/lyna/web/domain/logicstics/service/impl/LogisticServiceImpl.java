package com.lyna.web.domain.logicstics.service.impl;

import com.lyna.commons.infrustructure.object.RequestPage;
import com.lyna.commons.infrustructure.service.BaseService;
import com.lyna.web.domain.logicstics.DeliveryView;
import com.lyna.web.domain.logicstics.LogisticView;
import com.lyna.web.domain.logicstics.repository.LogisticViewRepository;
import com.lyna.web.domain.logicstics.service.LogisticService;
import com.lyna.web.domain.view.LogisticAggregate;
import com.lyna.web.domain.view.PackageAggregate;
import com.lyna.web.domain.view.PackageName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class LogisticServiceImpl extends BaseService implements LogisticService {


    @Autowired
    private LogisticViewRepository logisticViewRepository;

    @Override
    public Map<String, Object> findLogisticsView(int tenantId, RequestPage logisticRequestPage) {
        List<LogisticView> logisticView = this.logisticViewRepository.findLogistics(tenantId, logisticRequestPage);
        List<DeliveryView> deliveryView = this.logisticViewRepository.findDeliveries(tenantId, logisticRequestPage);

        Set<PackageName> pkgName = new HashSet<>();
        logisticView.stream().filter(el -> el.isPackageNameNonNull()).distinct().parallel()
                .forEach(el -> pkgName.add(new PackageName(el.getPackageName(), el.getAmount())));

        deliveryView.stream().filter(el -> el.isPackageNameNonNull()).distinct().parallel()
                .forEach(el -> pkgName.add(new PackageName(el.getPackageName(), el.getAmount())));


        Map<String, List<PackageAggregate>> deliveryPackagesByOrderId = groupDPackagesByOrderId(deliveryView);
        Map<String, List<PackageAggregate>> logisticPackagesByOrderId = groupLPackagesByOrderId(logisticView);
        // show override delivery.
        List<LogisticAggregate> aggregates = logisticView.stream()
                .parallel()
                .map(el -> LogisticAggregate.parseFromViewDTO(el, pkgName, groupLPackagesByOrderId(logisticView), deliveryPackagesByOrderId))
                .distinct()
                .collect(Collectors.toList());

        // show original delivery.
        List<LogisticAggregate> deliveryOriginalView = deliveryView.stream()
                .filter(el -> !logisticPackagesByOrderId.entrySet().contains(el.getOrderId()))
                .map(el -> LogisticAggregate.fromDeliveryView(el, pkgName ,deliveryPackagesByOrderId))
                .distinct()
                .collect(Collectors.toList());

        aggregates.addAll(deliveryOriginalView);
        aggregates.stream().sorted();

        HashMap<String, Object> val = new HashMap<>();
        val.put(LOGISTIC_DATA, aggregates);
        val.put(PKG_TYPE, new ArrayList<>(pkgName));
        return val;
    }

    private Map<String, List<PackageAggregate>> groupDPackagesByOrderId(List<DeliveryView> deliveryView) {
        return deliveryView.stream()
                    .parallel()
                    .map(PackageAggregate::fromDeliveryView) // to PackageAggregate
                    .collect(Collectors.groupingBy(PackageAggregate::getOrderId));
    }

    private Map<String, List<PackageAggregate>> groupLPackagesByOrderId(List<LogisticView> logisticView) {
        return logisticView.stream()
                    .parallel()
                    .map(PackageAggregate::fromLogisticView) // to PackageAggregate
                    .collect(Collectors.groupingBy(PackageAggregate::getOrderId));
    }

}
