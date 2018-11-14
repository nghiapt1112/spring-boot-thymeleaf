package com.lyna.web.domain.mpackage;

import com.lyna.commons.infrustructure.object.AbstractEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "m_package")
@NamedQueries({
        @NamedQuery(name = "Package.countAll", query = "SELECT COUNT(x) FROM Package x")
})
@Data
@NoArgsConstructor
public class Package extends AbstractEntity {

    @Id
    @Column(name = "pakage_id", nullable = false)
    public String pakageId;

    @Column
    private String name;

    @Column
    private String unit;

    @Column(name = "empty_weight")
    private BigDecimal emptyWeight;

    @Column(name = "full_load_weight")
    private BigDecimal fullLoadWeight;

    @Column(name = "empty_capacity")
    private BigDecimal emptyCapacity;

    @Column(name = "full_load_capacity")
    private BigDecimal fullLoadCapacity;

}