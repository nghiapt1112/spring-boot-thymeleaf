package com.lyna.web.domain.user;

import com.lyna.commons.infrustructure.object.AbstractEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "m_user_store_authority")
@Data
@NoArgsConstructor
public class UserStoreAuthority extends AbstractEntity {

    @Id
    @Column(name = "user_store_authority_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "store_id")
    private String storeId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "authority")
    private short authority;

    public String storeAuthorityToUserAuthority() {
        return StoreRoleType.fromVal(this.authority).name();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public short getAuthority() {
        return authority;
    }

    public void setAuthority(short authority) {
        this.authority = authority;
    }
}
