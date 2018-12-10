package com.lyna.web.domain.logicstics;

import com.lyna.commons.infrustructure.object.AbstractObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogisticDTO extends AbstractObject {

    private String storeId;
    private String storeName;
    private Date orderDate;
    private Integer totalOrder;
    private String postName;
    private String courseName;
    private BigDecimal amount;
    private String deliveryId;
    private String logisticId;

//    public LogisticDTO(String storeId, String storeName, Date orderDate, Integer totalOrder, String postName, String courseName, BigDecimal amount, String deliveryId, String logisticId) {
//        this.storeId = storeId;
//        this.storeName = storeName;
//        this.orderDate = orderDate;
//        this.totalOrder = totalOrder;
//        this.postName = postName;
//        this.courseName = courseName;
//        this.amount = amount;
//        this.deliveryId = deliveryId;
//        this.logisticId = logisticId;
//    }

    public boolean isDeliveryData() {
        return Objects.nonNull(this.deliveryId);
    }
}
