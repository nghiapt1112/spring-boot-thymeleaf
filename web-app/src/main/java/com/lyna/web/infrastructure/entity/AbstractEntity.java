//package com.lyna.web.infrastructure.entity;
//
//import com.lyna.commons.infrustructure.object.AbstractObject;
//import org.springframework.format.annotation.DateTimeFormat;
//
//import javax.persistence.Column;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.MappedSuperclass;
//import java.util.Date;
//
//@MappedSuperclass
//public class AbstractEntity extends AbstractObject {
//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE)
//    @Column(name = "id")
//    protected int id;
//
//    @Column(name = "modified_at")
//    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
//    protected Date modifiedAt;
//
//    @Column(name = "created_at")
//    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
//    protected Date createdAt;
//
//    @Column(name = "is_delete")
//    protected boolean isDeleted;
//
//    public int getId() {
//        return id;
//    }
//
//    public Date getCreatedAt() {
//        return createdAt;
//    }
//
//    public Date getModifiedAt() {
//        return modifiedAt;
//    }
//
//    public void initDefaultFieldsCreate() {
//        //TODO: create dateTimeUtils to get currentDate
//        this.createdAt= new Date();
//    }
//
//    public void initDefaultFieldsModify() {
//        this.modifiedAt = new Date();
//    }
//
//    public boolean isDeleted() {
//        return isDeleted;
//    }
//
//    public void setDeleted(boolean deleted) {
//        isDeleted = deleted;
//    }
//}
