package com.lyna.web.domain.view;

import com.lyna.commons.infrustructure.object.AbstractObject;
import com.lyna.web.domain.stores.Store;

public class StoreAggregate extends AbstractObject {
    private String orderDate;
    private String storeName;
    private String postName;
    private String productName;
    private int quantity;
    private String firstCategory;
    private String secondCategory;
    private String thirdCategory;

    public static StoreAggregate fromEntity(Store el) {
        StoreAggregate aggregate = new StoreAggregate();
//        aggregate.orderDate = el.getOrderDate().toString(); // TODO: convert using DateTimeUtils
//        aggregate.storeName = el.getPostCourseId()

        return aggregate;
    }
}
