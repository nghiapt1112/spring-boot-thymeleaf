package com.lyna.web.domain.view;

import com.lyna.commons.infrustructure.object.AbstractObject;
import com.lyna.web.domain.logicstics.LogisticDTO;
import com.lyna.web.domain.mpackage.Package;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.lyna.web.infrastructure.utils.DateTimeUtils.convertDateToString;

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

    private String logisticAmount;
    private String logisticPackageId;


    private String deliveryAmount;
    private String deliveryPackageId;
    private List<Package> packages;

    //    private String logisticPackageName;
//    private String logisticAmount;
//    private String deliveryAmount;
//    private String deliveryPackageName;
    private boolean deliveryData;


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
//        TODO: set logisticPackageId and deliveryPackageId
        aggregate.deliveryPackageId = dto.getDeliveryPackageId();
        aggregate.logisticPackageId = dto.getLogisticPackageId();
//        aggregate.logisticPackageName = dto.getLogisticPackageName();
//        aggregate.logisticAmount = dto.getLogisticAmount();
//        aggregate.deliveryAmount = dto.getDeliveryAmount();
//        aggregate.deliveryPackageName = dto.getDeliveryPackageName();

        return aggregate;
    }

    public void updatePackage(Map<String, Package> packageById) {
        if (Objects.isNull(this.packages)) {
            this.packages = new ArrayList<>();
        }

        Package deliveryPackage = null;
        if (this.deliveryData) {
            deliveryPackage = packageById.get(this.deliveryPackageId);
        } else {
            deliveryPackage = packageById.get(this.logisticPackageId);
        }
        if (Objects.isNull(deliveryPackage)) {
            // DATA ERROR
        }
        this.packages.add(deliveryPackage);
    }
}
