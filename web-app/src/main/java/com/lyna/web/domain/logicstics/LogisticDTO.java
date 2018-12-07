package com.lyna.web.domain.logicstics;

import com.lyna.commons.infrustructure.object.AbstractObject;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
public class LogisticDTO extends AbstractObject {
    private Date orderDate;
    private String storeName;
    private String postName;
    private String orderNumber;
    private BigDecimal amount;
    private String packaggeName; // cai nay phai tra ve 1 list, loop => list de hien thi tren table_tile
    private String totalWeigh;
    private String totalCapacity;
    private String courseName;

}
