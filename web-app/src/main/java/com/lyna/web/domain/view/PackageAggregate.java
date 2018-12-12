package com.lyna.web.domain.view;

import com.lyna.commons.infrustructure.object.AbstractObject;
import com.lyna.web.domain.delivery.DeliveryDetail;
import com.lyna.web.domain.logicstics.LogiticsDetail;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@EqualsAndHashCode
public class PackageAggregate extends AbstractObject {
    public BigDecimal amount;
    public String packageName;

    public static PackageAggregate fromDeliveryDetailEntity(DeliveryDetail deliveryDetail) {
        PackageAggregate aggregate = new PackageAggregate();
        aggregate.amount = deliveryDetail.getAmount();
        aggregate.packageName = deliveryDetail.getPack().getName();
        return aggregate;
    }

    public static PackageAggregate fromLogisticDetailEntity(LogiticsDetail logiticsDetail) {
        PackageAggregate aggregate = new PackageAggregate();
        aggregate.amount = logiticsDetail.getAmount();
        aggregate.packageName = logiticsDetail.getPack().getName();
        return aggregate;
    }
}
