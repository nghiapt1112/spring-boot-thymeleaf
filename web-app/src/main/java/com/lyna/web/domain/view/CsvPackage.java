package com.lyna.web.domain.view;

import com.lyna.web.domain.mpackage.Package;
import com.lyna.web.domain.user.User;
import com.opencsv.bean.CsvBindByName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@ToString
public class CsvPackage {

    @CsvBindByName(column = "荷姿名")
    private String packageName;

    @CsvBindByName(column = "単位")
    private String unit;

    @CsvBindByName(column = "空の重量")
    private BigDecimal emptyWeight;

    @CsvBindByName(column = "満載の重量")
    private BigDecimal fullLoadWeight;

    @CsvBindByName(column = "空の才数")
    private BigDecimal emptyCapacity;

    @CsvBindByName(column = "満載の才数")
    private BigDecimal fullLoadCapacity;

    public Package createPackage(User user) {
        Package p = new Package();
        p.setName(this.getPackageName());
        p.setUnit(this.getUnit());
        p.setEmptyWeight(this.getEmptyWeight());
        p.setFullLoadWeight(this.getFullLoadWeight());
        p.setEmptyCapacity(this.getEmptyCapacity());
        p.setFullLoadCapacity(this.getFullLoadCapacity());
        p.setTenantId(user.getTenantId());
        p.setCreateUser(user.getId());
        p.setCreateDate(new Date());
        return p;
    }

}
