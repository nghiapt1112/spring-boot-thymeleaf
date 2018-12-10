package com.lyna.web.domain.view;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Created by rajeevkumarsingh on 25/09/17.
 */
@Data
@NoArgsConstructor
@ToString
public class CsvOrder {
    @CsvBindByName
    private String name;

    @CsvBindByName(column = "日付")
    private String orderDate;

    @CsvBindByName(column = "店舗")
    private String store;

    @CsvBindByName(column = "便")
    private String post;

    @CsvBindByName(column = "商品")
    private String product;

    @CsvBindByName(column = "個数")
    private String quantity;

    @CsvBindByName(column = "大分類")
    private String category1;

    @CsvBindByName(column = "中分類")
    private String category2;

    @CsvBindByName(column = "小分類")
    private String category3;
}
