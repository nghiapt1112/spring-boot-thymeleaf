package com.lyna.web.domain.view;

import com.lyna.commons.infrustructure.object.AbstractObject;
import com.lyna.commons.utils.DateTimeUtils;
import com.lyna.web.domain.logicstics.MainMenuView;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class LogisticAggregate extends AbstractObject implements Comparable<LogisticAggregate> {
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

//    private boolean isDeliveryData;

    private MainMenuDataType type;

    private List<PackageName> packageWithNames;

    {
        totalWeight = new BigDecimal(0);
        totalCapacity = new BigDecimal(0);
    }

    public LogisticAggregate(MainMenuView logisticView) {
        this.parseCommonFields(logisticView);
    }

    private void parseCommonFields(MainMenuView logisticView) {
        this.orderId = logisticView.getOrderId();
        this.orderDate = DateTimeUtils.convertDateToString(logisticView.getOrderDate());
        this.storeName = logisticView.getStoreName();
        this.postName = logisticView.getPost();
        this.amount = logisticView.getAmount();
        this.price = logisticView.getPrice();
        this.courseName = logisticView.getCourse();
    }

    @Override
    public int compareTo(LogisticAggregate other) {
        return this.orderDate.compareToIgnoreCase(other.orderDate);
    }

    public static LogisticAggregate parseFromViewDTO(MainMenuView view, Collection<PackageName> totalPackageName,
                                                     Map<String, List<PackageAggregate>> logisticPackagesByOrderId,
                                                     Map<String, List<PackageAggregate>> deliveryPackagesByOrderId) {
        LogisticAggregate aggregate = new LogisticAggregate(view);

        List<PackageAggregate> deliveryPackages = deliveryPackagesByOrderId.get(aggregate.orderId);

        if (Objects.nonNull(deliveryPackages)) {
            // delivery Data
            aggregate.type = MainMenuDataType.DELIVERY_OVERRIDE;
            aggregate.packageWithNames = sumAmount(deliveryPackages);
            sumWeightAndCapacity(deliveryPackagesByOrderId, aggregate);
        } else {
            aggregate.packageWithNames = sumAmount(logisticPackagesByOrderId.get(aggregate.orderId));
            sumWeightAndCapacity(logisticPackagesByOrderId, aggregate);
        }
        fillPackages(totalPackageName, aggregate);

        return aggregate;
    }

    /**
     * make every aggregate object have same number of package to have got equal number of column in UI.
     */
    private static void fillPackages(Collection<PackageName> totalPackageName, LogisticAggregate aggregate) {
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
    }

    /**
     * sum all weigh and capacity for each order
     */
    private static void sumWeightAndCapacity(Map<String, List<PackageAggregate>> packageByOrderId, LogisticAggregate aggregate) {
        for (PackageAggregate el : packageByOrderId.get(aggregate.getOrderId())) {
            aggregate.totalWeight = aggregate.totalWeight.add(el.getFullLoadWeight());
            aggregate.totalCapacity = aggregate.totalCapacity.add(el.getFullLoadCapacity());
        }
    }

    /**
     * Return a list of map: packageName - sum(amount)
     */
    private static List<PackageName> sumAmount(List<PackageAggregate> packages) {
        if (Objects.isNull(packages)) {
            return null;
        }

        return packages.stream()
                .filter(el -> Objects.nonNull(el.getPackageName()))
                .collect(Collectors.groupingBy(
                        PackageAggregate::getPackageName,
                        Collectors.reducing(BigDecimal.ZERO, PackageAggregate::getAmount, BigDecimal::add)
                ))// create map: packageName-sum(amount)
                .entrySet().stream()
                .map(el -> new PackageName(el.getKey(), el.getValue()))
                .collect(Collectors.toList()); // convert map to list
    }

    public String getBackgroundColor() {
        if (this.type == MainMenuDataType.DELIVERY_OVERRIDE) {
            return "bg-green";
        } else if (this.type == MainMenuDataType.DELIVERY_ORIGINAL) {
            return "bg-orange-active";
        }
        return "";
    }

    public static LogisticAggregate fromDeliveryView(MainMenuView view, Collection<PackageName> pkgName, Map<String, List<PackageAggregate>> deliveryPackagesByOrderId) {
        LogisticAggregate aggregate = new LogisticAggregate(view);
        aggregate.type = MainMenuDataType.DELIVERY_ORIGINAL;
        aggregate.packageWithNames = sumAmount(deliveryPackagesByOrderId.get(aggregate.getOrderId()));
        sumWeightAndCapacity(deliveryPackagesByOrderId, aggregate);
        fillPackages(pkgName, aggregate);
        return aggregate;
    }

}
