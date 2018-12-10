package com.lyna.web.domain.order;

import com.lyna.commons.infrustructure.object.AbstractEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import java.math.BigDecimal;
import java.util.UUID;


@Entity
@Table(name = "t_order_detail")
@NamedQueries({
        @NamedQuery(name = "OrderDetail.countAll", query = "SELECT COUNT(x) FROM OrderDetail x")
})
@Data
public class OrderDetail extends AbstractEntity {
    @Id
    @Column(name = "order_detail_id", nullable = false)
    public String orderDetailId;

    @Column
    public BigDecimal amount;


    @Column(name = "order_id", nullable = false)
    public String orderId;

    @Column(name = "product_id")
    public String productId;

    public OrderDetail() {
        this.orderDetailId = UUID.randomUUID().toString();
    }

}