package com.lyna.web.domain.view;

import com.lyna.commons.infrustructure.object.AbstractObject;
import com.lyna.web.domain.logicstics.LogisticView;
import com.lyna.web.infrastructure.utils.DateTimeUtils;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class LogisticAggregate extends AbstractObject {
    private String orderId;

    private String orderDate;

    private String storeName;

    private String postName;

    private BigDecimal amount;

    private BigDecimal price;

    private Long totalPackage;

    private Long packageCase;

    private Long packageBox;

    private BigDecimal totalWeight;

    private BigDecimal totalCapacity;

    private String courseName;

    private boolean isDeliveryData;

    private List<PackageName> packageWithNames;

    {
        totalWeight = new BigDecimal(0);
        totalCapacity = new BigDecimal(0);
    }
    public static LogisticAggregate parseFromViewDTO(LogisticView logisticView,
                                                     Map<String, List<PackageAggregate>> logisticPackagesByOrderId,
                                                     Map<String, List<PackageAggregate>> deliveryPackagesByOrderId) {
        LogisticAggregate aggregate = new LogisticAggregate();
        aggregate.orderId = logisticView.getOrderId();
        aggregate.orderDate = DateTimeUtils.convertDateToString(logisticView.getOrderDate());
        aggregate.storeName = logisticView.getStoreName();
        aggregate.postName = logisticView.getPost();
        aggregate.amount = logisticView.getAmount();
        aggregate.price = logisticView.getPrice();
        aggregate.courseName = logisticView.getCourse();


        List<PackageAggregate> deliveryPackage = deliveryPackagesByOrderId.get(aggregate.orderId);

        if (Objects.nonNull(deliveryPackage)) {
            // delivery Data
            aggregate.isDeliveryData = true;
            aggregate.packageWithNames = parsePackage(deliveryPackage);
            fillWithPackageInfo(deliveryPackagesByOrderId, aggregate);
        } else {
            aggregate.packageWithNames = parsePackage(logisticPackagesByOrderId.get(aggregate.orderId));
            fillWithPackageInfo(logisticPackagesByOrderId, aggregate);
        }

        return aggregate;
    }

    private static void fillWithPackageInfo(Map<String, List<PackageAggregate>> logisticPackagesByOrderId, LogisticAggregate aggregate) {
        for( PackageAggregate el : logisticPackagesByOrderId.get(aggregate.getOrderId())) {
            aggregate.totalWeight = aggregate.totalWeight.add(el.getFullLoadWeight());
            aggregate.totalCapacity = aggregate.totalCapacity.add(el.getFullLoadCapacity());
        }
    }

    private static List<PackageName> parsePackage(List<PackageAggregate> packages) {
        if (Objects.isNull(packages)) {
            return null;
        }

        Map<String, BigDecimal> packageAmountByName = packages.stream()
                .filter(el -> Objects.nonNull(el.getPackageName()))
                .collect(Collectors.groupingBy(
                        PackageAggregate::getPackageName,
                        Collectors.reducing(BigDecimal.ZERO, PackageAggregate::getAmount, BigDecimal::add)
                ));

        return packageAmountByName.entrySet().stream()
                .map(el -> new PackageName(el.getKey(), el.getValue()))
                .sorted(Comparator.comparing(PackageName::getName))
                .collect(Collectors.toList());
    }
}

