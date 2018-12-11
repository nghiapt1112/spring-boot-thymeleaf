package com.lyna.web.domain.logicstics;

import com.lyna.commons.infrustructure.object.AbstractEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

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

}
