package com.lyna.commons.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public final class DateTimeUtils {
    public static final String DD_MM_YYYY = "yyyy-MM-dd";
    private static final String DEFAULT_DATE_FORMAT = "yyyy/MM/dd";
    private static final String DATE_FORMAT = "yyyy/MM/dd";
    private static final String DATE_FORMAT_VI = "yyyy-MM-dd";

    public static String convertDateToString(Date date, String style) {
        SimpleDateFormat format = new SimpleDateFormat(style);
        return format.format(date);
    }

    public static Date getDafaultDate() {
        Date date = new Date();
        return date;
    }

    public static Date converStringToDate(String dateInString) {
        Date date = null;

        if (dateInString.contains("-")) {
            SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT_VI, Locale.ENGLISH);

            try {
                date = formatter.parse(dateInString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (dateInString.contains("/")) {
            SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
            try {
                date = formatter.parse(dateInString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return date;
    }

    public static String convertDateToString(Date date) {
        SimpleDateFormat format = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        return format.format(date);
    }

    public static Date getDateWithoutTimeUsingCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    public static Date getDateMaxTimeUsingCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 59);

        return calendar.getTime();
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
