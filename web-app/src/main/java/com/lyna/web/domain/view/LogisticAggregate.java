package com.lyna.web.domain.view;

import com.lyna.commons.infrustructure.object.AbstractObject;
import com.lyna.web.domain.delivery.DeliveryDetail;
import com.lyna.web.domain.logicstics.LogisticDTO;
import com.lyna.web.domain.logicstics.LogiticsDetail;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.lyna.web.infrastructure.utils.DateTimeUtils.convertDateToString;
import static com.lyna.web.domain.view.PackageAggregate.fromDeliveryDetailEntity;
import static com.lyna.web.domain.view.PackageAggregate.fromLogisticDetailEntity;
@Data
@NoArgsConstructor
public class LogisticAggregate extends AbstractObject {
    private String storeId;
    private String orderDate;
    private Integer totalOrder;
    private String storeName;
    private String postName;
    private String courseName;
    private BigDecimal amount;

    private List<PackageAggregate> deliveryDetails;
    private boolean deliveryData;

    private String deliveryId;
    private String logisticId;


    public static LogisticAggregate fromLogisticDTO(LogisticDTO dto) {
        LogisticAggregate aggregate = new LogisticAggregate();
        aggregate.storeId = dto.getStoreId();
        aggregate.orderDate = convertDateToString(dto.getOrderDate());
        aggregate.totalOrder = dto.getTotalOrder();
        aggregate.storeName = dto.getStoreName();
        aggregate.postName = dto.getPostName();
        aggregate.courseName = dto.getCourseName();
        aggregate.amount = dto.getAmount();


        if (dto.isDeliveryData()) {
            aggregate.deliveryData = true;
        }
        aggregate.logisticId = dto.getLogisticId();
        aggregate.deliveryId = dto.getDeliveryId();

        return aggregate;
    }

    public void updatePackage(Map<String, List<LogiticsDetail>> logisticDetailsById, Map<String, List<DeliveryDetail>> deliveryDetailsById) {
        if (Objects.isNull(this.deliveryDetails)) {
            this.deliveryDetails = new ArrayList<>();
        }
        if (this.deliveryData) {
            for ( DeliveryDetail deliveryDetail: deliveryDetailsById.get(this.deliveryId)) {
                this.deliveryDetails.add(fromDeliveryDetailEntity(deliveryDetail));
            }
        } else {
            for (LogiticsDetail el : logisticDetailsById.get(this.logisticId)) {
                this.deliveryDetails.add(fromLogisticDetailEntity(el));
            }
        }

    }
}

