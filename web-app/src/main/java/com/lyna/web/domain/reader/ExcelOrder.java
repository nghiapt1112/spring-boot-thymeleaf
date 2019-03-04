package com.lyna.web.domain.reader;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class ExcelOrder {
    private String orderDate;
    private String storeCode;
    private String storeName;
    private String post;
    private String productCode;
    private String productName;
    private String quantity;
    private String category1;
    private String category2;
    private String category3;
}