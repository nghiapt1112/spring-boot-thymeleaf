package com.lyna.web.infrastructure.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class DateTimeUtils {
    public static final String DD_MM_YYYY = "yyyy-MM-dd";
    public static final String DEFAULT_DATE_FORMAT = "yyyy/mm/dd";


//    public static String formatDate(String date, String initDateFormat, String endDateFormat) throws ParseException {
//        Date initDate = new SimpleDateFormat(initDateFormat).parse(date);
//        SimpleDateFormat formatter = new SimpleDateFormat(endDateFormat);
//        String parsedDate = formatter.format(initDate);
//        return parsedDate;
//    }

    public static String convertDateToString(Date date, String style) {
        SimpleDateFormat format = new SimpleDateFormat(style);
        return format.format(date);
    }

    public static String convertDateToString(Date date) {
        SimpleDateFormat format = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        return format.format(date);
    }

    public static Date getCurrentDate() {
        return new Date();
    }

    public static Date convertStringToDate(String dateStr) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return format.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

     public static Date fromNumber(Long val) {
        return new Date(val);
    }

}
