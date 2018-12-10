package com.lyna.web.domain.order;

import com.lyna.commons.infrustructure.object.AbstractEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
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

    //    @JsonIgnore
    @OneToMany
    @JoinColumn(name = "order_id")
    private Set<OrderDetail> orderDetails;

    public Order() {
        this.orderId = UUID.randomUUID().toString();
    }
}