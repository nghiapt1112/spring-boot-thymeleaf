package com.lyna.web.domain.delivery;

import com.lyna.commons.infrustructure.object.AbstractEntity;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "t_delivery_detail")
@NamedQueries({
        @NamedQuery(name = "DeliveryDetail.countAll", query = "SELECT COUNT(x) FROM DeliveryDetail x")
})
@Data
public class DeliveryDetail extends AbstractEntity {

    @Id
    @Column(name = "delivery_detail_id", nullable = false)
    public String deliveryDetailId;

    @Column
    public BigDecimal amount;

    @Column(name = "delivery_id", nullable = false)
    public String deliveryId;

    @Column(name = "package_id", nullable = false)
    public String packageId;

    public DeliveryDetail() {
        this.deliveryDetailId = UUID.randomUUID().toString();
    }
}