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

    @CsvBindByName(column = "日付/ Ngày tháng", required = true)
    private String orderDate;

    @CsvBindByName(column = "店舗/ Store")
    private String store;

    @CsvBindByName(column = "便/ Post")
    private String post;

    @CsvBindByName(column = "商品/ Product")
    private String product;

    @CsvBindByName(column = "個数/ Số lượng")
    private String quantity;

    @CsvBindByName(column = "大分類/ Phân loại lớn")
    private String category1;
    @CsvBindByName(column = "中分類/ Phân loại vừa")
    private String category2;
    @CsvBindByName(column = "小分類/ Phân loại nhỏ")
    private String category3;
}
