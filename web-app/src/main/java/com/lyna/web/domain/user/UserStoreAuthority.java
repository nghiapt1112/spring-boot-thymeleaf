package com.lyna.web.domain.user;

import com.lyna.commons.infrustructure.object.AbstractEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "m_user_store_authority")
@Data
public class UserStoreAuthority extends AbstractEntity {

    @Id
    @Column(name = "user_store_authority_id", nullable = false)
    private String id;

    @Column(name = "store_id")
    private String storeId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "authority")
    private short authority;

    public UserStoreAuthority() {
        this.id = UUID.randomUUID().toString();
    }

    public String storeAuthorityToUserAuthority() {
        return StoreRoleType.fromVal(this.authority).name();
    }

    public void initDefaultCreateFields(User currentUser) {
        this.tenantId = currentUser.getTenantId();
        this.initDefaultFieldsCreate();
        this.createUser = currentUser.getId();
    }

    public void initDefaultUpdateFields(User currentUser) {
        this.initDefaultFieldsUpdate();
        this.updateUser = currentUser.getId();
    }

    public void hideSensitiveFields() {
        this.id = null;
        this.storeId = null;
        this.userId = null;
    }
}
