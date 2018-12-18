package com.lyna.web.domain.order;

import com.lyna.commons.infrustructure.object.AbstractObject;
import com.lyna.commons.utils.DateTimeUtils;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class OrderAggregate extends AbstractObject {
    private String orderId;

    private String orderDate;

    private String storeName;

    private String postName;

    private String productName;

    private BigDecimal orderAmount;

    private String firstCategory;

    private String secondCategory;

    private String thirdCategory;

    public static OrderAggregate parseFromView(OrderView orderView) {
        OrderAggregate aggregate = new OrderAggregate();
        aggregate.orderId = orderView.getOrderId();
        aggregate.orderDate = DateTimeUtils.converDateToString(orderView.getOrderDate());
        aggregate.storeName = orderView.getStoreName();
        aggregate.postName = orderView.getPostName();
        aggregate.productName = orderView.getProductName();
        aggregate.orderAmount = orderView.getOrderAmount();
        aggregate.firstCategory = orderView.getFirstCategory();
        aggregate.secondCategory = orderView.getSecondCategory();
        aggregate.thirdCategory = orderView.getThirdCategory();
        return aggregate;
    }
}
