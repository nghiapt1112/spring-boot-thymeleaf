package com.lyna.web.domain.logicstics;

import com.lyna.commons.infrustructure.object.AbstractEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public String logisticsDetailId;

    @Column
    public BigDecimal amount;

    @Column(name = "package_id", nullable = false)
    public String packageId;

    @Column(name = "logistics_id", nullable = false)
    public String logisticsId;

}