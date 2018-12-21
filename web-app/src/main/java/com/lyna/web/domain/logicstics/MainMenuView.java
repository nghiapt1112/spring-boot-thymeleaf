package com.lyna.web.domain.logicstics;

import com.lyna.commons.infrustructure.object.AbstractObject;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.math.BigDecimal;
import java.util.Date;

@MappedSuperclass
@Data
public class MainMenuView extends AbstractObject {
    @Column
    protected int tenantId;

    @Column(name = "order_id", nullable = false)
    public String orderId;

    @Column
    protected Date orderDate;

    @Column
    protected String storeName;

    @Column
    protected String post;

    @Column
    protected BigDecimal amount;

    @Column
    protected BigDecimal price;

    @Column
    protected String packageId;

    @Column
    protected String packageName;

    @Column(name = "full_load_weight")
    protected BigDecimal totalWeight;

    @Column(name = "full_load_capacity")
    protected BigDecimal totalCapacity;

    @Column
    protected String course;

}
