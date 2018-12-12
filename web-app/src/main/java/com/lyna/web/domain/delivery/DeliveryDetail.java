package com.lyna.web.domain.delivery;

import com.lyna.commons.infrustructure.object.AbstractEntity;
import com.lyna.web.domain.mpackage.Package;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "t_delivery_detail")
@NamedQueries({
        @NamedQuery(name = "DeliveryDetail.countAll", query = "SELECT COUNT(x) FROM DeliveryDetail x")
})
@Data
@NoArgsConstructor
public class DeliveryDetail extends AbstractEntity {

    @Id
    @Column(name = "delivery_detail_id", nullable = false)
    public String deliveryDetailId;

    @Column
    public BigDecimal amount;

    @Column(name = "delivery_id", nullable = false)
    public String deliveryId;

    @Column(name = "package_id", nullable = false)
    public String packageId;

    @OneToOne
    @JoinColumn(name = "package_id", referencedColumnName = "package_id", insertable = false, updatable = false)
    public Package pack;
}