package com.lyna.web.domain.logicstics.service.impl;

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
    public Map<String, Object> findLogisticsView(int tenantId) {
        List<LogisticView> logisticView = this.logisticViewRepository.findLogistics(tenantId);
        List<DeliveryView> deliveryView = this.logisticViewRepository.findDeliveries(tenantId);

        Map<String, List<PackageAggregate>> logisticPackagesByOrderId = logisticView.stream()
                .parallel()
                .map(PackageAggregate::fromLogisticView) // to PackageAggregate
                .collect(Collectors.groupingBy(PackageAggregate::getOrderId)); // group packageByOrderId

        Map<String, List<PackageAggregate>> deliveryPackagesByOrderId = deliveryView.stream()
                .parallel()
                .map(PackageAggregate::fromDeliveryView) // to PackageAggregate
                .collect(Collectors.groupingBy(PackageAggregate::getOrderId)); // group packageByOrderId


        Set<PackageName> pkgName = new HashSet<>();
        logisticView.stream().filter(el -> el.isPackageNameNonNull()).distinct().parallel()
                .forEach(el -> pkgName.add(new PackageName(el.getPackageName(), el.getAmount())));

        deliveryView.stream().filter(el -> el.isPackageNameNonNull()).distinct().parallel()
                .forEach(el -> pkgName.add(new PackageName(el.getPackageName(), el.getAmount())));


        List<LogisticAggregate> aggregates = logisticView.stream()
                .parallel()
                .map(el -> LogisticAggregate.parseFromViewDTO(el, pkgName, logisticPackagesByOrderId, deliveryPackagesByOrderId))
                .collect(Collectors.toList());

        HashMap<String, Object> val = new HashMap<>();
        val.put(LOGISTIC_DATA, aggregates);
        val.put(PKG_TYPE, new ArrayList<>(pkgName));
        return val;
    }

}
