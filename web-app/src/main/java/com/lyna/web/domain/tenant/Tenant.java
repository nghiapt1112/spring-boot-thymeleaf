package com.lyna.web.domain.tenant;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "m_tenant")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@NamedQueries({
        @NamedQuery(name = "Tenant.countAll", query = "SELECT COUNT(x) FROM Tenant x")
})
public class Tenant implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "tenant_id", nullable = false)
    public int tenantId;


    @Basic
    @Column(name = "name", nullable = true, length = 255)
    public String name;


    @Basic
    @Column(name = "user_limit_number", nullable = false)
    public int userLimitNumber;


    @Basic
    @Column(name = "create_date", nullable = true)
    public Timestamp createDate;


    @Basic
    @Column(name = "created_user_id", nullable = true)
    public Integer createdUserId;


    @Basic
    @Column(name = "update_date", nullable = true)
    public Timestamp updateDate;


    @Basic
    @Column(name = "update_user_id", nullable = true)
    public Integer updateUserId;

    public int getTenantId() {
        return tenantId;
    }

    public void setTenantId(int tenantId) {
        this.tenantId = tenantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUserLimitNumber() {
        return userLimitNumber;
    }

    public void setUserLimitNumber(int userLimitNumber) {
        this.userLimitNumber = userLimitNumber;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public Integer getCreatedUserId() {
        return createdUserId;
    }

    public void setCreatedUserId(Integer createdUserId) {
        this.createdUserId = createdUserId;
    }

    public Timestamp getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Timestamp updateDate) {
        this.updateDate = updateDate;
    }

    public Integer getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(Integer updateUserId) {
        this.updateUserId = updateUserId;
    }

    @Override
    public String toString() {
        return "Tenant{" +
                "tenantId=" + tenantId +
                ", name='" + name + '\'' +
                ", userLimitNumber=" + userLimitNumber +
                ", createDate=" + createDate +
                ", createdUserId=" + createdUserId +
                ", updateDate=" + updateDate +
                ", updateUserId=" + updateUserId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tenant)) return false;
        Tenant tenant = (Tenant) o;
        return tenantId == tenant.tenantId &&
                userLimitNumber == tenant.userLimitNumber &&
                Objects.equals(name, tenant.name) &&
                Objects.equals(createDate, tenant.createDate) &&
                Objects.equals(createdUserId, tenant.createdUserId) &&
                Objects.equals(updateDate, tenant.updateDate) &&
                Objects.equals(updateUserId, tenant.updateUserId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tenantId, name, userLimitNumber, createDate, createdUserId, updateDate, updateUserId);
    }
}
