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
import java.util.Objects;

@Entity
@Table(name = "v_logicstic")
@Data
@NoArgsConstructor
public class LogisticView extends AbstractObject {

    @Id
    @Column(name = "order_id")
    private String orderId;

    @Column
    private int tenantId;

    @Column
    private String logistics_id;

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

    public boolean isPackageNameNonNull() {
        return Objects.nonNull(packageName);
    }
}
