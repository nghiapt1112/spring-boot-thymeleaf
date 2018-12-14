package com.lyna.web.domain.order;

import com.lyna.commons.infrustructure.object.AbstractObject;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "v_order")
@Data
@NoArgsConstructor
public class OrderView extends AbstractObject {
    @Id
    @Column
    private String orderId;

    @Column
    private int tenantId;

    @Column
    private Date orderDate;

    @Column
    private String storeName;

    @Column
    private String postName;

    @Column
    private String productName;

    @Column
    private BigDecimal orderAmount;

    @Column
    private String firstCategory;

    @Column
    private String secondCategory;

    @Column
    private String thirdCategory;
}
