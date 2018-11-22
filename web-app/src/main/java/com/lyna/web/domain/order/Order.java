package com.lyna.web.domain.order;

import com.lyna.commons.infrustructure.object.AbstractEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.sql.Timestamp;

@Entity
@Table(name = "t_order")
@NamedQueries({
        @NamedQuery(name = "Order.countAll", query = "SELECT COUNT(x) FROM Order x")
})
@Data
@NoArgsConstructor
public class Order extends AbstractEntity {
    @Id
    @Column(name = "order_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public String orderId;

    @Column(name = "order_date")
    public Timestamp orderDate;

    @Column
    public String post;

}