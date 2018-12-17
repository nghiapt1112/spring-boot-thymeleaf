package com.lyna.web.domain.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lyna.commons.infrustructure.object.AbstractEntity;
import com.lyna.web.domain.product.Product;
import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;
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

    @JsonIgnore
    @OneToMany
    @JoinColumn(name = "product_id")
    private Set<Product> products;

    public OrderDetail() {
        this.orderDetailId = UUID.randomUUID().toString();
    }

}