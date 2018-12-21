package com.lyna.web.domain.view;

import com.lyna.commons.infrustructure.object.AbstractObject;
import com.lyna.web.domain.logicstics.DeliveryView;
import com.lyna.web.domain.logicstics.LogisticView;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Objects;

@Data
@NoArgsConstructor
@EqualsAndHashCode
public class PackageAggregate extends AbstractObject {

    private String orderId;
    private String packageId;
    private String packageName;
    private BigDecimal fullLoadWeight;
    private BigDecimal fullLoadCapacity;
    private BigDecimal amount;

    public static PackageAggregate fromLogisticView(LogisticView logisticView) {
        PackageAggregate aggregate = new PackageAggregate();
        aggregate.orderId = logisticView.getOrderId();
        aggregate.packageName = logisticView.getPackageName();
        aggregate.packageId = logisticView.getPackageId();
        aggregate.fullLoadWeight = logisticView.getTotalWeight();
        aggregate.fullLoadCapacity = logisticView.getTotalCapacity();
        aggregate.amount = logisticView.getPackageAmount();
        return aggregate;
    }

    public static PackageAggregate fromDeliveryView(DeliveryView deliveryView) {
        PackageAggregate aggregate = new PackageAggregate();
        aggregate.orderId = deliveryView.getOrderId();
        aggregate.packageName = deliveryView.getPackageName();
        aggregate.packageId = deliveryView.getPackageId();
        aggregate.fullLoadWeight = deliveryView.getTotalWeight();
        aggregate.fullLoadCapacity = deliveryView.getTotalCapacity();
        aggregate.amount = deliveryView.getPackageAmount();
        return aggregate;
    }

    public BigDecimal getAmount() {
        return Objects.isNull(amount) ? new BigDecimal(0) : amount;
    }

    public boolean packageNameNotNull() {
        return Objects.nonNull(this.packageName);
    }

    public BigDecimal getFullLoadWeight() {
        return Objects.isNull(fullLoadWeight) ? new BigDecimal(0) : fullLoadWeight;
    }

    public BigDecimal getFullLoadCapacity() {
        return Objects.isNull(fullLoadCapacity) ? new BigDecimal(0) : fullLoadCapacity;
    }
}
