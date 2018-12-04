package com.lyna.web.domain.view;

import com.lyna.commons.infrustructure.object.AbstractObject;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Collection;

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

}
