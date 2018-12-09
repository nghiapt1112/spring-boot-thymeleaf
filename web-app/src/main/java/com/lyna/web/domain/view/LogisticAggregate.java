package com.lyna.web.domain.view;

import com.lyna.commons.infrustructure.object.AbstractObject;
import com.lyna.web.domain.logicstics.LogisticDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import static com.lyna.web.infrastructure.utils.DateTimeUtils.convertDateToString;

@Data
@NoArgsConstructor
public class LogisticAggregate extends AbstractObject {
    private String storeId;
    private String orderDate;
    private Integer totalOrder;
    private String storeName;
    private String postName;
    private String courseName;
    private BigDecimal amount;
//    private String logisticPackageName;
//    private String logisticAmount;
//    private String deliveryAmount;
//    private String deliveryPackageName;
    private boolean deliveryData;


    public static LogisticAggregate fromLogisticDTO(LogisticDTO dto) {
        LogisticAggregate aggregate = new LogisticAggregate();
        aggregate.storeId = dto.getStoreId();
        aggregate.orderDate = convertDateToString(dto.getOrderDate());
        aggregate.totalOrder = dto.getTotalOrder();
        aggregate.storeName = dto.getStoreName();
        aggregate.postName = dto.getPostName();
        aggregate.courseName = dto.getCourseName();
        aggregate.amount = dto.getAmount();
//        aggregate.logisticPackageName = dto.getLogisticPackageName();
//        aggregate.logisticAmount = dto.getLogisticAmount();
//        aggregate.deliveryAmount = dto.getDeliveryAmount();
//        aggregate.deliveryPackageName = dto.getDeliveryPackageName();

        return aggregate;
    }

}
