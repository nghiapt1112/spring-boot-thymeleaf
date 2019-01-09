package com.lyna.web.domain.view;

import com.lyna.web.domain.product.Product;
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
public class CsvProduct {
    @CsvBindByName(column = "商品コード")
    private String productCode;

    @CsvBindByName(column = "商品名")
    private String productName;

    @CsvBindByName(column = "単位")
    private String unit;

    @CsvBindByName(column = "単価")
    private BigDecimal unitPrice;

    @CsvBindByName(column = "大分類")
    private String category1;

    @CsvBindByName(column = "中分類")
    private String category2;

    @CsvBindByName(column = "小分類")
    private String category3;

    public Product createProduct(User user) {
        Product product = new Product();
        product.setCode(this.getProductCode());
        product.setName(this.getProductName());
        product.setUnit(this.getUnit());
        product.setPrice(this.getUnitPrice());
        product.setCategory1(this.getCategory1());
        product.setCategory2(this.getCategory2());
        product.setCategory3(this.getCategory3());
        product.setOrderDetails(product.getOrderDetails());
        product.setTenantId(user.getTenantId());
        product.setCreateUser(user.getId());
        product.setCreateDate(new Date());
        return product;
    }
}
