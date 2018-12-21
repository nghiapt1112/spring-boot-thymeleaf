package com.lyna.commons.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

public class DataUtils {

    private static final String DATE_FORMAT = "yyyy/MM/dd";
    private static final String DATE_FORMAT_VI = "yyyy-MM-dd";

    public static boolean isNumeric(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
