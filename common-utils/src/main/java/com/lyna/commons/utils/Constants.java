package com.lyna.commons.utils;

public final class Constants {
    public static final String HEADER_FILE_ORDER = "header.csv";
    public static final String PARSE_CSV_FAILED = "CSVのデータが不正。";
    public static final String TENANT_ID = "tenantId";
    public static final String POST_COURSE_ID = "postCourseId";
    public static final String NOT_UPDATE_DATA = "0";
    public static final String UPDATE_DATA = "1";

    public static final class ENTITY_STATUS {
        public static final Integer CREATED = 1;
        public static final Integer DELETED = 2;
        public static final Integer UPDATED = 3;
        public static final Integer IMPORT = 4;
    }
}
