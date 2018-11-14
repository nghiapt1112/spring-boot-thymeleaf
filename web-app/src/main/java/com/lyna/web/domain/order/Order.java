package com.lyna.web.domain.order;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "t_order")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@NamedQueries({
        @NamedQuery(name = "Order.countAll", query = "SELECT COUNT(x) FROM Order x")
})
public class Order implements Serializable {
    @Id
    @Column(name = "order_id", nullable = false, length = 36)
    public String orderId;

    @Basic
    @Column(name = "order_date", nullable = true)
    public Timestamp orderDate;


    @Basic
    @Column(name = "post", nullable = true, length = 50)
    public String post;


    @Basic
    @Column(name = "create_date", nullable = true)
    public Timestamp createDate;


    @Basic
    @Column(name = "create_user", nullable = true, length = 36)
    public String createUser;

    @Basic
    @Column(name = "update_date", nullable = true)
    public Timestamp updateDate;


    @Basic
    @Column(name = "update_user", nullable = true, length = 36)
    public String updateUser;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Timestamp getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Timestamp updateDate) {
        this.updateDate = updateDate;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId='" + orderId + '\'' +
                ", orderDate=" + orderDate +
                ", post='" + post + '\'' +
                ", createDate=" + createDate +
                ", createUser='" + createUser + '\'' +
                ", updateDate=" + updateDate +
                ", updateUser='" + updateUser + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        Order order = (Order) o;
        return Objects.equals(orderId, order.orderId) &&
                Objects.equals(orderDate, order.orderDate) &&
                Objects.equals(post, order.post) &&
                Objects.equals(createDate, order.createDate) &&
                Objects.equals(createUser, order.createUser) &&
                Objects.equals(updateDate, order.updateDate) &&
                Objects.equals(updateUser, order.updateUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, orderDate, post, createDate, createUser, updateDate, updateUser);
    }
}
