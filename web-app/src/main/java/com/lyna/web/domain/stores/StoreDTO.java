package com.lyna.web.domain.stores;

import com.lyna.commons.infrustructure.object.AbstractObject;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
public class StoreDTO extends AbstractObject {
    private Date orderDate;
    private String storeName;
    private String postName;
    private String productName;
    private int amount;
    private BigDecimal productPrice;
    private String firstCategory;
    private String secondCategory;
    private String thirdCategory;

    public StoreDTO(Date orderDate, String storeName, String postName, String productName, int amount, BigDecimal productPrice,
                    String firstCategory, String secondCategory, String thirdCategory) {
        this.orderDate = orderDate;
        this.storeName = storeName;
        this.postName = postName;
        this.productName = productName;
        this.amount = amount;
        this.productPrice = productPrice;
        this.firstCategory = firstCategory;
        this.secondCategory = secondCategory;
        this.thirdCategory = thirdCategory;
    }

}
