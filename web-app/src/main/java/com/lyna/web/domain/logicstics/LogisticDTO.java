package com.lyna.web.domain.logicstics;

import com.lyna.commons.infrustructure.object.AbstractObject;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
public class LogisticDTO extends AbstractObject {

    private String storeId;
    private Date orderDate;
    private Integer totalOrder;
    private String storeName;
    private String postName;
    private String courseName;
    private BigDecimal amount;

    private String logisticPackageName;
    private String logisticAmount;

    private String deliveryAmount;
    private String deliveryPackageName;


    public LogisticDTO(String storeId, Date orderDate, Integer totalOrder, String storeName, String postName,
                       String courseName, BigDecimal amount, String logisticPackageName, String logisticAmount,
                       String deliveryAmount, String deliveryPackageName) {
        this.storeId = storeId;
        this.orderDate = orderDate;
        this.totalOrder = totalOrder;
        this.storeName = storeName;
        this.postName = postName;
        this.courseName = courseName;
        this.amount = amount;
        this.logisticPackageName = logisticPackageName;
        this.logisticAmount = logisticAmount;
        this.deliveryAmount = deliveryAmount;
        this.deliveryPackageName = deliveryPackageName;
    }
}
