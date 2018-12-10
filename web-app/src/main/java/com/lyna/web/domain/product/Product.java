package com.lyna.web.domain.product;

import com.lyna.commons.infrustructure.object.AbstractEntity;
import com.lyna.web.domain.order.OrderDetail;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "m_product")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@NamedQueries({
        @NamedQuery(name = "Product.countAll", query = "SELECT COUNT(x) FROM Product x")
})
@Data
public class Product extends AbstractEntity {

    @Id
    @Column(name = "product_id", nullable = false)
    private String productId;


    @NotBlank(message = "商品コードは必須です。")
    @Column
    private String code;

    @NotBlank(message = "商品名は必須です。")
    @Column
    private String name;

    @Column
    private String unit;

    @Column
    @NotNull(message = "単価を正しく入力してください。")
    private BigDecimal price;

    @Column
    private String category1;

    @Column
    private String category2;

    @Column
    private String category3;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private Set<OrderDetail> orderDetails;

    public Product(){
        this.productId = UUID.randomUUID().toString();
    }
}