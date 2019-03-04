package com.lyna.commons.utils;

public final class Constants {
    public static final String HEADER_FILE_ORDER = "header.csv";
    public static final String PARSE_CSV_FAILED = "CSVのデータが不正。";
    public static final String TENANT_ID = "tenantId";
    public static final String POST_COURSE_ID = "postCourseId";
    public static final String NOT_UPDATE_DATA = "0";
    public static final String UPDATE_DATA = "1";

    public static final class AI_STATUS {
        public static final Integer EMPTY = 2;
        public static final Integer ERROR = 1;
        public static final Integer SUCCESS = 0;
    }


    public static final class ENTITY_STATUS {
        public static final Integer CREATED = 1;
        public static final Integer DELETED = 2;
        public static final Integer UPDATED = 3;
        public static final Integer IMPORT = 4;
    }

    public static final class FILE_EXTENSION {
        public static final Integer EXCEL = 1;
        public static final Integer CSV = 2;
        public static final String EXCEL_XLSX= "xlsx";
        public static final String EXCEL_XLS= "xls";
    }

    public static final class FILE_DELIVERY {
        public static final Integer ORDER_DATE = 0;
        public static final Integer STORE_CODE = 1;
        public static final Integer STORE_NAME = 2;
        public static final Integer POST = 3;
        public static final Integer TRAY = 4;
        public static final Integer CASE = 5;
        public static final Integer CARD_BOX = 6;
        public static final Integer BOX = 7;
    }
}
