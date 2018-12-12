package com.lyna.web.domain.view;

import com.lyna.commons.infrustructure.object.AbstractObject;
import com.lyna.web.domain.logicstics.DeliveryView;
import com.lyna.web.domain.logicstics.LogisticView;
import com.lyna.web.infrastructure.utils.DateTimeUtils;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

@Data
@NoArgsConstructor
public class LogisticAggregate extends AbstractObject {
    private String orderId;

    private String orderDate;

    private String storeName;

    private String postName;

    private BigDecimal amount;

    private BigDecimal price;

    private Long totalPackage = -1L;

    private Long packageCase = -1L;

    private Long packageBox = -1L;

    private BigDecimal totalWeight;

    private BigDecimal totalCapacity;

    private String courseName;

    private boolean isDeliveryData;

    public static LogisticAggregate parseFromViewDTO(LogisticView logisticView, Map<String, DeliveryView> deliveryViewByOrderId) {
        LogisticAggregate aggregate = new LogisticAggregate();
        aggregate.orderId = logisticView.getOrderId();
        aggregate.orderDate = DateTimeUtils.convertDateToString(logisticView.getOrderDate());
        aggregate.storeName = logisticView.getStoreName();
        aggregate.postName = logisticView.getPostName();
        aggregate.amount = logisticView.getAmount();
        aggregate.price = logisticView.getPrice();
        aggregate.courseName = logisticView.getCourseName();

        DeliveryView deliveryView = deliveryViewByOrderId.get(aggregate.orderId);
        if (Objects.nonNull(deliveryView)) {
            // delivery Data
            aggregate.isDeliveryData = true;
            aggregate.totalPackage = deliveryView.getTotalPackage();
            aggregate.packageCase = deliveryView.getPackageCase();
            aggregate.packageBox = deliveryView.getPackageBox();
            aggregate.totalWeight = deliveryView.getTotalWeight();
            aggregate.totalCapacity = deliveryView.getTotalCapacity();
        } else {
            aggregate.totalPackage = logisticView.getTotalPackage();
            aggregate.packageCase = logisticView.getPackageCase();
            aggregate.packageBox = logisticView.getPackageBox();
            aggregate.totalWeight = logisticView.getTotalWeight();
            aggregate.totalCapacity = logisticView.getTotalCapacity();
        }
        return aggregate;
    }
}

