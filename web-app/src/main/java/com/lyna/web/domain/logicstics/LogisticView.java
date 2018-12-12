package com.lyna.web.domain.logicstics;

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
@Table(name = "v_logicstic")
@Data
@NoArgsConstructor
public class LogisticView extends AbstractObject {

    @Id
    @Column(name = "order_id")
    private String orderId;

    @Column
    private Date orderDate;

    @Column
    private String storeName;

    @Column
    private String postName;

    @Column
    private BigDecimal amount;

    @Column
    private BigDecimal price;

    @Column
    private Long totalPackage;

    @Column
    private Long packageCase;

    @Column
    private Long packageBox;

    @Column
    private BigDecimal totalWeight;

    @Column
    private BigDecimal totalCapacity;

    @Column
    private String courseName;
}
