package com.lyna.web.domain.logicstics;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "t_logistics_detail")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@NamedQueries({
        @NamedQuery(name = "LogicticsDetail.countAll", query = "SELECT COUNT(x) FROM LogicticsDetail x")
})
public class LogicticsDetail implements Serializable {
    @Id
    @Column(name = "logistics_detail_id", nullable = false, length = 11)
    public String logisticsDetailId;


    @Basic
    @Column(name = "amount", nullable = true, precision = 0)
    public Integer amount;


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

    public String getLogisticsDetailId() {
        return logisticsDetailId;
    }

    public void setLogisticsDetailId(String logisticsDetailId) {
        this.logisticsDetailId = logisticsDetailId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
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
        return "LogicticsDetail{" +
                "logisticsDetailId='" + logisticsDetailId + '\'' +
                ", amount=" + amount +
                ", createDate=" + createDate +
                ", createUser='" + createUser + '\'' +
                ", updateDate=" + updateDate +
                ", updateUser='" + updateUser + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LogicticsDetail)) return false;
        LogicticsDetail that = (LogicticsDetail) o;
        return Objects.equals(logisticsDetailId, that.logisticsDetailId) &&
                Objects.equals(amount, that.amount) &&
                Objects.equals(createDate, that.createDate) &&
                Objects.equals(createUser, that.createUser) &&
                Objects.equals(updateDate, that.updateDate) &&
                Objects.equals(updateUser, that.updateUser);
    }

    @Override
    public int hashCode() {
        return Objects.hash(logisticsDetailId, amount, createDate, createUser, updateDate, updateUser);
    }
}
