package com.lyna.web.domain.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lyna.commons.infrustructure.object.AbstractEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;
import java.util.UUID;


@Entity
@Table(name = "t_order")
@NamedQueries({
        @NamedQuery(name = "Order.countAll", query = "SELECT COUNT(x) FROM Order x")
})
@Data
public class Order extends AbstractEntity {
    @Id
    @Column(name = "order_id", nullable = false)
    public String orderId;

    @Column(name = "order_date")
    public Date orderDate;

    @Column(name = "post_course_id")
    public String postCourseId;

    @JsonIgnore
    @OneToMany
    @JoinColumn(name = "order_id")
    private Set<OrderDetail> orderDetails;

    public Order() {
        this.orderId = UUID.randomUUID().toString();
    }
}