package com.lyna.commons.infrustructure.object;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@MappedSuperclass
public class AbstractEntity extends AbstractObject {
//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE)
//    @Column(name = "id")
//    protected int id;

    @Column(name = "tenant_id")
    protected int tenantId;

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

    public void initDefaultFieldsCreate() {
        //TODO: create dateTimeUtils to get currentDate
        this.createDate = new Date();
    }

    public void initDefaultFieldsModify() {
        this.updateDate = new Date();
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

    public int getTenantId() {
        return tenantId;
    }

    public void setTenantId(int tenantId) {
        this.tenantId = tenantId;
    }

    @JsonIgnore
    public <E extends AbstractEntity > E withTenantId(int tenantId) {
        this.tenantId = tenantId;
        return (E) this;
    }
}
