package com.lyna.web.domain.logicstics;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "v_delivery")
@Data
@NoArgsConstructor
public class DeliveryView {
    @Id
    @Column(name = "order_id")
    private String orderId;

    @Column
    private String delivery_id;

    @Column
    private Date orderDate;

    @Column
    private String storeName;

    @Column
    private String post;

    @Column
    private BigDecimal amount;

    @Column
    private BigDecimal price;

    @Column
    private String packageId;

    @Column
    private String packageName;

    @Column(name = "full_load_weight")
    private BigDecimal totalWeight;

    @Column(name = "full_load_capacity")
    private BigDecimal totalCapacity;

    @Column
    private String course;
}
