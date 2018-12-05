package com.lyna.web.domain.view;

import com.lyna.commons.infrustructure.object.AbstractObject;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Random;
import java.util.UUID;

@Data
@NoArgsConstructor
public class LogisticAggregate extends AbstractObject {
    private String orderDate;
    private String storeName;
    private int post;
    private int orderQuantity;
    private BigDecimal totalAmount;
    private Collection<Package> packages;
    private float weight;
    private float capacity;
    private String courseName;
    private boolean logisticData;

    public LogisticAggregate initTestData() {
        this.orderDate = "dd/MM/yyyy";
        this.storeName = "test.storeName".concat(UUID.randomUUID().toString());
        this.post = randomInt(1, 100);
        this.orderQuantity = randomInt(1, 100);
        this.totalAmount = new BigDecimal("123");
        this.weight = randomInt(100, 1000);
        this.capacity = randomInt(100, 1000);
        this.courseName = "course.name".concat(UUID.randomUUID().toString());
//        this.logisticData = false;
        return this;

    }

    private int randomInt(int start,int  end) {
        return new Random()
                .ints(start, end)
                .findFirst()
                .getAsInt();
    }
}
