package com.lyna.web.domain.view;

import com.lyna.commons.infrustructure.object.AbstractObject;
import com.lyna.commons.utils.DataUtils;
import com.lyna.commons.utils.DateTimeUtils;
import com.lyna.web.domain.logicstics.LogisticView;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
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

    public static LogisticAggregate parseFromViewDTO(LogisticView logisticView, Collection<PackageName> totalPackageName,
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

        //Fill all package_type into each row to make each row will have equal package column.
        List<PackageName> oldPkgWithName = new ArrayList<>(aggregate.packageWithNames);
        for (PackageName pkgName : totalPackageName) {
            PackageName checkingPkg = oldPkgWithName.stream().filter(el -> el.getName().equals(pkgName.getName()))
                    .findFirst().orElse(null);
            if (Objects.isNull(checkingPkg)) {
                aggregate.packageWithNames.add(new PackageName(pkgName.getName(), pkgName.getAmount()));
            }
        }
        aggregate.packageWithNames.stream().sorted(Comparator.comparing(PackageName::getName));
        return aggregate;
    }

    /**
     * sum all weigh and capacity for each order
     */
    private static void fillWithPackageInfo(Map<String, List<PackageAggregate>> packageByOrderId, LogisticAggregate aggregate) {
        for (PackageAggregate el : packageByOrderId.get(aggregate.getOrderId())) {
            aggregate.totalWeight = aggregate.totalWeight.add(el.getFullLoadWeight());
            aggregate.totalCapacity = aggregate.totalCapacity.add(el.getFullLoadCapacity());
        }
    }

    private static List<PackageName> parsePackage(List<PackageAggregate> packages) {
        if (Objects.isNull(packages)) {
            return null;
        }

        return packages.stream()
                .filter(el -> Objects.nonNull(el.getPackageName())) // packageName not null
                .collect(Collectors.groupingBy(
                        PackageAggregate::getPackageName,
                        Collectors.reducing(BigDecimal.ZERO, PackageAggregate::getAmount, BigDecimal::add)
                ))// create map: name-totalAmount
                .entrySet().stream()
                .map(el -> new PackageName(el.getKey(), el.getValue()))
                .collect(Collectors.toList());
    }

    public List<String> getPackageTypes() {
        if (CollectionUtils.isEmpty(this.packageWithNames)) {
            return Collections.EMPTY_LIST;
        } else {
            return this.packageWithNames.stream().map(PackageName::getName).collect(Collectors.toList());
        }
    }
}

