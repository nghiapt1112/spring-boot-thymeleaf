package com.lyna.web.domain.delivery;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "t_delivery")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@NamedQueries({
        @NamedQuery(name = "Delivery.countAll", query = "SELECT COUNT(x) FROM Delivery x")
})
public class Delivery implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "delivery_id", nullable = false, length = 36)
    public String deliveryId;


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

    public String getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(String deliveryId) {
        this.deliveryId = deliveryId;
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
        return "Delivery{" +
                "deliveryId='" + deliveryId + '\'' +
                ", createDate=" + createDate +
                ", createUser='" + createUser + '\'' +
                ", updateDate=" + updateDate +
                ", updateUser='" + updateUser + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Delivery)) return false;
        Delivery delivery = (Delivery) o;
        return Objects.equals(getDeliveryId(), delivery.getDeliveryId()) &&
                Objects.equals(getCreateDate(), delivery.getCreateDate()) &&
                Objects.equals(getCreateUser(), delivery.getCreateUser()) &&
                Objects.equals(getUpdateDate(), delivery.getUpdateDate()) &&
                Objects.equals(getUpdateUser(), delivery.getUpdateUser());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getDeliveryId(), getCreateDate(), getCreateUser(), getUpdateDate(), getUpdateUser());
    }
}
