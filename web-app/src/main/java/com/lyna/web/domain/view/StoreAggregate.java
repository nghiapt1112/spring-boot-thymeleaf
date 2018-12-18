package com.lyna.web.domain.view;

import com.lyna.commons.infrustructure.object.AbstractObject;
import com.lyna.commons.utils.DataUtils;
import com.lyna.commons.utils.DateTimeUtils;
import com.lyna.web.domain.stores.StoreDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class StoreAggregate extends AbstractObject {
    private String orderDate;
    private String storeName;
    private String postName;
    private String productName;
    private int amount;
    private String productPrice;
    private String firstCategory;
    private String secondCategory;
    private String thirdCategory;

    public static StoreAggregate fromDTO(StoreDTO el) {
        StoreAggregate aggregate = new StoreAggregate();
        aggregate.orderDate = DateTimeUtils.convertDateToString(el.getOrderDate());
        aggregate.storeName = el.getStoreName();
        aggregate.postName = el.getPostName();
        aggregate.productName = el.getProductName();
        aggregate.amount = el.getAmount();
        aggregate.productPrice = el.getProductPrice().toString();
        aggregate.firstCategory = el.getFirstCategory();
        aggregate.secondCategory = el.getSecondCategory();
        aggregate.thirdCategory = el.getThirdCategory();
        return aggregate;
    }
}
