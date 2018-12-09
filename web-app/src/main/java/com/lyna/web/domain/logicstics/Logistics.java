package com.lyna.web.domain.logicstics;

import com.lyna.commons.infrustructure.object.AbstractEntity;
import com.lyna.web.domain.stores.Store;
import com.lyna.web.domain.stores.StoreDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "t_logistics")
@NamedQueries({
        @NamedQuery(name = "Logistics.countAll", query = "SELECT COUNT(x) FROM Logistics x")
})
@Data
@SqlResultSetMapping(
        name = Logistics.MAIN_MENU_LOGISTIC_DELIVERY_LIST,
        classes = {
                @ConstructorResult(
                        targetClass = LogisticDTO.class,
                        columns = {
                                @ColumnResult(name = "storeId", type = String.class),
                                @ColumnResult(name = "orderDate", type = Date.class),
                                @ColumnResult(name = "totalOrder", type = Integer.class),
                                @ColumnResult(name = "storeName", type = String.class),
                                @ColumnResult(name = "postName", type = String.class),
                                @ColumnResult(name = "courseName", type = String.class),
                                @ColumnResult(name = "amount", type = BigDecimal.class),
                                @ColumnResult(name = "logisticPackageName", type = String.class),
                                @ColumnResult(name = "logisticAmount", type = String.class),
                                @ColumnResult(name = "deliveryAmount", type = String.class),
                                @ColumnResult(name = "deliveryPackageName", type = String.class)
                        }
                )
        }
)
@NoArgsConstructor
public class Logistics extends AbstractEntity {
    public static final String MAIN_MENU_LOGISTIC_DELIVERY_LIST = "logistic_delivery";
    @Id
    @Column(name = "logistics_id", nullable = false)
    public String id;

    @Column(name = "order_id", nullable = false)
    public String orderId;

}
