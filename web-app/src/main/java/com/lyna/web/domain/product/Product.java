package com.lyna.web.domain.product;

import com.lyna.commons.infrustructure.object.AbstractEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "m_product")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@NamedQueries({
        @NamedQuery(name = "Product.countAll", query = "SELECT COUNT(x) FROM Product x")
})
@Data
@NoArgsConstructor
public class Product extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "productId", nullable = false)
    private String productId;

    @Column
    private String code;

    @Column
    private String name;

    @Column
    private String unit;

    @Column
    private BigDecimal price;

    @Column
    private String category1;

    @Column
    private String category2;

    @Column
    private String category3;

    @Column
    private String temperature;

}