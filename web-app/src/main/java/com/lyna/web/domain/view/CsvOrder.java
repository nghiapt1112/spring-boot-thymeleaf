package com.lyna.web.domain.view;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class CsvOrder {
    @CsvBindByName(column = "店舗")
    private String storeName;

    @CsvBindByName(column = "日付")
    private String orderDate;

    @CsvBindByName(column = "店舗コード")
    private String storeCode;

    @CsvBindByName(column = "便")
    private String post;

    @CsvBindByName(column = "商品コード")
    private String productCode;

    @CsvBindByName(column = "商品")
    private String productName;

    @CsvBindByName(column = "個数")
    private String quantity;

    @CsvBindByName(column = "大分類")
    private String category1;

    @CsvBindByName(column = "中分類")
    private String category2;

    @CsvBindByName(column = "小分類")
    private String category3;
}