package com.lyna.web.domain.logicstics;

import com.lyna.commons.infrustructure.object.AbstractEntity;
import com.lyna.commons.utils.DateTimeUtils;
import com.lyna.web.domain.user.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "t_logistics_detail")
@NamedQueries({
        @NamedQuery(name = "LogiticsDetail.countAll", query = "SELECT COUNT(x) FROM LogiticsDetail x")
})
@Data
@NoArgsConstructor
public class LogiticsDetail extends AbstractEntity {

    @Id
    @Column(name = "logistics_detail_id", nullable = false)
    public String logisticsDetailId;

    @Column
    public BigDecimal amount;

    @Column(name = "package_id", nullable = false)
    public String packageId;

    @Column(name = "logistics_id", nullable = false)
    public String logisticsId;

    public LogiticsDetail(User currentUser, Integer amount, String packageId, String logisticsId) {
        this.logisticsDetailId = UUID.randomUUID().toString();
        this.createUser = currentUser.getId();
        this.tenantId = currentUser.getTenantId();
        this.createDate = DateTimeUtils.getCurrentDateTime();
        this.updateAmount(amount);
        this.packageId = packageId;
        this.logisticsId = logisticsId;
    }


    public void updateAmount(Integer amount) {
        this.amount = new BigDecimal(amount);
    }

    public void updateInfo(User currentUser, Integer amount) {
        this.updateAmount(amount);
        this.updateDate = DateTimeUtils.getCurrentDateTime();
        this.updateUser = currentUser.getId();
    }
}