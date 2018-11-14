package com.lyna.web.domain.delivery;

import com.lyna.commons.infrustructure.object.AbstractEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "t_delivery_detail")
@NamedQueries({
        @NamedQuery(name = "DeliveryDetail.countAll", query = "SELECT COUNT(x) FROM DeliveryDetail x")
})
@Data
@NoArgsConstructor
public class DeliveryDetail extends AbstractEntity {

    @Id
    @Column(name = "delivery_detail_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public String deliveryDetailId;

    @Column
    public BigDecimal amount;

}