package com.lyna.web.domain.logicstics;

import com.lyna.commons.infrustructure.object.AbstractObject;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Data
@NoArgsConstructor
public class LogisticDTO extends AbstractObject {

    private String storeId;
    private String storeName;
    private Date orderDate;
    private Integer totalOrder;
    private String postName;
    private String courseName;
    private BigDecimal amount;

    private String logisticAmount;
    private String logisticPackageId;


    private String deliveryAmount;
    private String deliveryPackageId;

    public LogisticDTO(String storeId, String storeName, Date orderDate, Integer totalOrder, String postName,
                       String courseName, BigDecimal amount, String logisticAmount, String logisticPackageId,
                       String deliveryAmount, String deliveryPackageId) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.orderDate = orderDate;
        this.totalOrder = totalOrder;
        this.postName = postName;
        this.courseName = courseName;
        this.amount = amount;
        this.logisticAmount = logisticAmount;
        this.logisticPackageId = logisticPackageId;
        this.deliveryAmount = deliveryAmount;
        this.deliveryPackageId = deliveryPackageId;
    }

    public boolean isDeliveryData() {
        return Objects.nonNull(this.deliveryAmount) && Objects.nonNull(this.deliveryPackageId);
    }
}
