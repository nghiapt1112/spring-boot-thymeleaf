package com.lyna.web.security.domain;

import com.lyna.commons.infrustructure.object.AbstractObject;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "m_tenant")
public class Tenant extends AbstractObject {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "tenant_id")
    protected int tenantId;

    @Column
    private String name;

    @Column(name = "user_limit_number")
    private Integer userLimitNumber;


    @Column(name = "update_date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    protected Date updateDate;

    @Column(name = "create_date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    protected Date createDate;


    @Column(name = "create_user")
    protected String createUser;

    @Column(name = "update_user")
    protected String updateUser;


    public Tenant() {
    }

    public Tenant(int tenantId, String name) {
        this.tenantId = tenantId;
        this.name = name;
    }

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

    public Integer getUserLimitNumber() {
        return userLimitNumber;
    }

    public void setUserLimitNumber(Integer userLimitNumber) {
        this.userLimitNumber = userLimitNumber;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }
}
