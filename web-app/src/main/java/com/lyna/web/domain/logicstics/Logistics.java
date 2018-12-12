package com.lyna.web.domain.logicstics;

import com.lyna.commons.infrustructure.object.AbstractEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
