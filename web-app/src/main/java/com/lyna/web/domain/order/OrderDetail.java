package com.lyna.web.domain.order;

import com.lyna.commons.infrustructure.object.AbstractEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.math.BigDecimal;


@Entity
@Table(name = "t_order_detail")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@NamedQueries({
        @NamedQuery(name = "OrderDetail.countAll", query = "SELECT COUNT(x) FROM OrderDetail x")
})
@Data
@NoArgsConstructor
public class OrderDetail extends AbstractEntity {
    @Id
    @Column(name = "order_detail_id", nullable = false)
    public String orderDetailId;

    @Column
    public BigDecimal amount;

}