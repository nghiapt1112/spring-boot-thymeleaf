package com.lyna.web.domain.order;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lyna.commons.infrustructure.object.AbstractEntity;
import com.lyna.web.domain.product.Product;
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
import java.math.BigDecimal;
import java.util.Set;


@Entity
@Table(name = "t_order_detail")
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

    @JsonIgnore
    @OneToMany
    @JoinColumn(name = "product_id")
    private Set<Product> products;
}