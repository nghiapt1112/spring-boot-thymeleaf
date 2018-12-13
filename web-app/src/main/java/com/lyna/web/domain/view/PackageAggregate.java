package com.lyna.web.domain.view;

import com.lyna.commons.infrustructure.object.AbstractObject;
import com.lyna.web.domain.logicstics.LogisticView;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

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

//    public static PackageAggregate fromDeliveryDetailEntity(DeliveryDetail deliveryDetail) {
//        PackageAggregate aggregate = new PackageAggregate();
//        aggregate.amount = deliveryDetail.getAmount();
//        aggregate.packageName = deliveryDetail.getPack().getName();
//        return aggregate;
//    }

    public static PackageAggregate fromLogisticView(LogisticView logisticView) {
        PackageAggregate aggregate = new PackageAggregate();
        aggregate.orderId = logisticView.getOrderId();
        aggregate.packageName = logisticView.getPackageName();
        aggregate.packageId = logisticView.getPackageId();
        aggregate.fullLoadWeight = logisticView.getTotalWeight();
        aggregate.fullLoadCapacity = logisticView.getTotalCapacity();
        aggregate.amount = logisticView.getAmount();
        return aggregate;
    }
}
