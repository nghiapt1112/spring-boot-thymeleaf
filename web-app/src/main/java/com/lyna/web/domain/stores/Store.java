package com.lyna.web.domain.stores;

import com.lyna.commons.infrustructure.object.AbstractEntity;
import com.lyna.web.domain.postCourse.PostCourse;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.NamedQueries;
import org.hibernate.annotations.NamedQuery;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "m_store")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@NamedQueries({
        @NamedQuery(name = "Store.getAll", query = "SELECT c FROM Store c WHERE c.tenantId = :tenantId ORDER BY c.name")
})
@SqlResultSetMapping(
        name = Store.MAIN_MENU_STORE_ORDER_LIST,
        classes = {
                @ConstructorResult(
                        targetClass = StoreDTO.class,
                        columns = {
                                @ColumnResult(name = "orderDate", type = Date.class),
                                @ColumnResult(name = "storeName", type = String.class),
                                @ColumnResult(name = "postName", type = String.class),
                                @ColumnResult(name = "productName", type = String.class),
                                @ColumnResult(name = "amount", type = Integer.class),
                                @ColumnResult(name = "productPrice", type = BigDecimal.class),
                                @ColumnResult(name = "firstCategory", type = String.class),
                                @ColumnResult(name = "secondCategory", type = String.class),
                                @ColumnResult(name = "thirdCategory", type = String.class)
                        }
                )
        }
)
@Data
public class Store extends AbstractEntity {
    public static final String MAIN_MENU_STORE_ORDER_LIST = "store_order";

    @Id
    @Column(name = "store_id", nullable = false)
    private String storeId;

    @NotBlank(message = "'店舗コード'は必須です。")
    @Column
    private String code;

    @NotBlank(message = "'店舗名'は必須です。")
    @Column
    private String name;

    @Column(name = "major_area")
    private String majorArea;

    @Column
    private String area;

    @Column
    private String address;

    @Column(name = "person_in_charge")
    private String personCharge;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Valid
    @OneToMany
    @JoinColumn(name = "store_id")
    private List<PostCourse> postCourses;

    public Store() {
        this.storeId = UUID.randomUUID().toString();
    }

}

