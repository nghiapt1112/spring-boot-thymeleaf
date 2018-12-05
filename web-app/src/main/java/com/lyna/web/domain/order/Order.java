package com.lyna.web.domain.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lyna.commons.infrustructure.object.AbstractEntity;
import com.lyna.web.domain.postCourse.PostCourse;
import com.lyna.web.domain.user.UserStoreAuthority;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.sql.Timestamp;
import java.util.Set;

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

    @Column(name = "post_course_id")
    public String postCourseId;

//    @JsonIgnore
    @OneToMany
    @JoinColumn(name = "order_id")
    private Set<OrderDetail> orderDetails;

}