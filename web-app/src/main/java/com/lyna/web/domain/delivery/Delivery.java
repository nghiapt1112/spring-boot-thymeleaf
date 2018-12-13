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
import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "t_delivery")
@NamedQueries({
        @NamedQuery(name = "Delivery.countAll", query = "SELECT COUNT(x) FROM Delivery x")
})
@Data
public class Delivery extends AbstractEntity {

    @Id
    @Column(name = "delivery_id", nullable = false)   
    public String deliveryId;


    @Column(name = "order_id", nullable = false)
    public String orderId;

    public Delivery() {
        this.deliveryId = UUID.randomUUID().toString();
    }
}