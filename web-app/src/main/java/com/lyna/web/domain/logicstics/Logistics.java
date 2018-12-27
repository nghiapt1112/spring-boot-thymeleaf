package com.lyna.web.domain.logicstics;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lyna.commons.infrustructure.object.AbstractEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

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

    @JsonIgnore
    @OneToMany
    @JoinColumn(name = "logistics_id")
    private Set<LogiticsDetail> logiticsDetails;

}
