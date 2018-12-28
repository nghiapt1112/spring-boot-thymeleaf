package com.lyna.web.domain.logicstics;

import com.lyna.commons.infrustructure.object.AbstractEntity;
import com.lyna.commons.utils.DateTimeUtils;
import com.lyna.web.domain.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Table(name = "t_logistics")
@NamedQueries({
        @NamedQuery(name = "Logistics.countAll", query = "SELECT COUNT(x) FROM Logistics x")
})
@Data
@NoArgsConstructor
public class Logistics extends AbstractEntity {
    @Id
    @Column(name = "logistics_id", nullable = false)
    public String logisticsId;

    @Column(name = "order_id", nullable = false)
    public String orderId;

    public Logistics(User currentUser, String orderId) {
        this.logisticsId = UUID.randomUUID().toString();
        this.orderId = orderId;
        this.createDate = DateTimeUtils.getCurrentDateTime();
        this.createUser = currentUser.getId();
        this.tenantId = currentUser.getTenantId();
    }
}
