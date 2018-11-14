package com.lyna.web.domain.mpackage;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "m_package")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@NamedQueries ( {
        @NamedQuery ( name="Package.countAll", query="SELECT COUNT(x) FROM Package x" )
})
public class Package implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "pakage_id", nullable = false, length = 36)
    public String pakageId;

    @Column(name="name", length=255)
    private String packageName;

    @Column(name="tenant_id", length=11)
    private Integer tenantId;

    @Column(name="unit", length=255)
    private String unit;

    @Column(name="empty_weight", length=11)
    private Double emptyWeight;

    @Column(name="full_load_weight", length=11)
    private Double fullLoadWeight;

    @Column(name="empty_capacity", length=11)
    private Double emptyCapacity;

    @Column(name="full_load_capacity", length=11)
    private Double fullLoadCapacity;

    @Column(name="create_date", length=11 , nullable = true)
    private Double createDate;

    @Column(name = "create_user", nullable = true, length = 36)
    public String createUser;

    @Column(name = "update_date", nullable = true)
    public Timestamp updateDate;

    @Column(name = "update_user", nullable = true, length = 36)
    public String updateUser;

    public String getPakageId() {
        return pakageId;
    }

    public void setPakageId(String pakageId) {
        this.pakageId = pakageId;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Integer getTenantId() {
        return tenantId;
    }

    public void setTenantId(Integer tenantId) {
        this.tenantId = tenantId;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Double getEmptyWeight() {
        return emptyWeight;
    }

    public void setEmptyWeight(Double emptyWeight) {
        this.emptyWeight = emptyWeight;
    }

    public Double getFullLoadWeight() {
        return fullLoadWeight;
    }

    public void setFullLoadWeight(Double fullLoadWeight) {
        this.fullLoadWeight = fullLoadWeight;
    }

    public Double getEmptyCapacity() {
        return emptyCapacity;
    }

    public void setEmptyCapacity(Double emptyCapacity) {
        this.emptyCapacity = emptyCapacity;
    }

    public Double getFullLoadCapacity() {
        return fullLoadCapacity;
    }

    public void setFullLoadCapacity(Double fullLoadCapacity) {
        this.fullLoadCapacity = fullLoadCapacity;
    }

    public Double getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Double createDate) {
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
        return "Package{" +
                "pakageId='" + pakageId + '\'' +
                ", packageName='" + packageName + '\'' +
                ", tenantId=" + tenantId +
                ", unit='" + unit + '\'' +
                ", emptyWeight=" + emptyWeight +
                ", fullLoadWeight=" + fullLoadWeight +
                ", emptyCapacity=" + emptyCapacity +
                ", fullLoadCapacity=" + fullLoadCapacity +
                ", createDate=" + createDate +
                ", createUser='" + createUser + '\'' +
                ", updateDate=" + updateDate +
                ", updateUser='" + updateUser + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Package)) return false;
        Package aPackage = (Package) o;
        return Objects.equals(getPakageId(), aPackage.getPakageId()) &&
                Objects.equals(getPackageName(), aPackage.getPackageName()) &&
                Objects.equals(getTenantId(), aPackage.getTenantId()) &&
                Objects.equals(getUnit(), aPackage.getUnit()) &&
                Objects.equals(getEmptyWeight(), aPackage.getEmptyWeight()) &&
                Objects.equals(getFullLoadWeight(), aPackage.getFullLoadWeight()) &&
                Objects.equals(getEmptyCapacity(), aPackage.getEmptyCapacity()) &&
                Objects.equals(getFullLoadCapacity(), aPackage.getFullLoadCapacity()) &&
                Objects.equals(getCreateDate(), aPackage.getCreateDate()) &&
                Objects.equals(getCreateUser(), aPackage.getCreateUser()) &&
                Objects.equals(getUpdateDate(), aPackage.getUpdateDate()) &&
                Objects.equals(getUpdateUser(), aPackage.getUpdateUser());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPakageId(), getPackageName(), getTenantId(), getUnit(), getEmptyWeight(), getFullLoadWeight(), getEmptyCapacity(), getFullLoadCapacity(), getCreateDate(), getCreateUser(), getUpdateDate(), getUpdateUser());
    }
}
