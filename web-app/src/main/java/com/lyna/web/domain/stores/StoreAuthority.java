package com.lyna.web.domain.stores;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "m_user_store_authority")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@NamedQueries({
        @NamedQuery(name = "StoreAuthority.countAll", query = "SELECT COUNT(x) FROM StoreAuthority x")
})
public class StoreAuthority implements Serializable {
    private static final long serialVersionUID = 1L;

    @Basic
    @Column(name = "name", nullable = true, length = 50)
    public String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "StoreAuthority{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
