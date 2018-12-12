package com.lyna.web.domain.delivery;

import com.lyna.commons.infrustructure.object.AbstractEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "t_delivery")
@NamedQueries({
        @NamedQuery(name = "Delivery.countAll", query = "SELECT COUNT(x) FROM Delivery x")
})
@Data
@NoArgsConstructor
public class Delivery extends AbstractEntity {

    @Id
    @Column(name = "delivery_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public String deliveryId;


    @Column(name = "order_id", nullable = false)
    public String orderId;
}