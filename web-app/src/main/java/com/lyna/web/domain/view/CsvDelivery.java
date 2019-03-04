package com.lyna.web.domain.view;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@ToString
public class CsvDelivery {
    @CsvBindByName(column = "店舗")
    private String storeName;

    @CsvBindByName(column = "日付")
    private String orderDate;

    @CsvBindByName(column = "店舗コード")
    private String storeCode;

    @CsvBindByName(column = "便")
    private String post;

    @CsvBindByName(column = "ばんじゅう")
    private String tray;

    @CsvBindByName(column = "箱")
    private String box;

    @CsvBindByName(column = "ケース")
    private String caseP;

    @CsvBindByName(column = "段ボール")
    private String cardBox;
}
