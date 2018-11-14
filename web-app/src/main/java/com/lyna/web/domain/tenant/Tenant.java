package com.lyna.web.domain.tenant;

import com.lyna.commons.infrustructure.object.AbstractObject;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "m_tenant")
@NamedQueries({
        @NamedQuery(name = "Tenant.countAll", query = "SELECT COUNT(x) FROM Tenant x")
})
@Data
@NoArgsConstructor
public class Tenant extends AbstractObject {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "tenant_id", nullable = false)
    protected int tenantId;

    @Column
    private String name;

    @Column(name = "user_limit_number")
    private Integer userLimitNumber;

    public Tenant(int tenantId, String name) {
        this.tenantId = tenantId;
        this.name = name;
    }

}
