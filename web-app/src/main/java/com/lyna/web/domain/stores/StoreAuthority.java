//package com.lyna.web.domain.stores;
//
//import com.lyna.commons.infrustructure.object.AbstractEntity;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//import org.hibernate.annotations.Cache;
//import org.hibernate.annotations.CacheConcurrencyStrategy;
//
//import javax.persistence.*;
//import java.io.Serializable;
//import java.util.Objects;
//
//@Entity
//@Table(name = "m_user_store_authority")
//@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
//@NamedQueries({
//        @NamedQuery(name = "StoreAuthority.countAll", query = "SELECT COUNT(x) FROM StoreAuthority x")
//})
//@Data
//@NoArgsConstructor
//public class StoreAuthority extends AbstractEntity {
//    @Basic
//    @Column
//    public String name;
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    @Override
//    public String toString() {
//        return "StoreAuthority{" +
//                "name='" + name + '\'' +
//                '}';
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(name);
//    }
//}
