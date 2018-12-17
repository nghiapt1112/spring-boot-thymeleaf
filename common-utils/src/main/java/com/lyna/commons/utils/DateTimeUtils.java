package com.lyna.commons.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public final class DateTimeUtils {
    public static final String DD_MM_YYYY = "yyyy-MM-dd";
    private static final String DEFAULT_DATE_FORMAT = "yyyy/MM/dd";

    public static String convertDateToString(Date date, String style) {
        SimpleDateFormat format = new SimpleDateFormat(style);
        return format.format(date);
    }

    public static String convertDateToString(Date date) {
        SimpleDateFormat format = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        return format.format(date);
    }

    public static String converDateToString(Date date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT);
        Instant instant = date.toInstant();
        LocalDateTime ldt = instant
                .atZone(ZoneId.of("CET"))
                .toLocalDateTime();
        return ldt.format(formatter);
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
