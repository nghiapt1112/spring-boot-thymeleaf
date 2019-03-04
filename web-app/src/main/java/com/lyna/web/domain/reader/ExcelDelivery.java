package com.lyna.web.domain.reader;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class ExcelDelivery {
    private String orderDate;

    private String storeCode;

    private String storeName;

    private String post;

    private String tray;

    private String box;

    private String caseP;

    private String cardBox;
}
