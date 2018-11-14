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
@Table(name = "t_logicstics")
@NamedQueries({
        @NamedQuery(name = "Logicstics.countAll", query = "SELECT COUNT(x) FROM Logicstics x")
})
@Data
@NoArgsConstructor
public class Logicstics extends AbstractEntity {
    @Id
    @Column(name = "logistics_id", nullable = false)
    public String logisticsId;

}
