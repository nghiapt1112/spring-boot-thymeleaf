package com.lyna.commons.utils;

import java.math.BigDecimal;

public class NumberUtils {

    public static BigDecimal removeTrailingZero(BigDecimal number) {
        BigDecimal bigDecimal = null;
        if (number != null) {
            String stringNumber = number.toString();
            int index = stringNumber.indexOf(".");
            if (!"0".equals(stringNumber.substring(index + 2))) {
                return number;
            } else {
                String resultNumber;
                if (!"0".equals(stringNumber.substring(index + 1, index + 2))) {
                    resultNumber = stringNumber.substring(0, stringNumber.length() - 1);
                    bigDecimal = new BigDecimal(resultNumber);
                    return bigDecimal;
                } else {
                    resultNumber = stringNumber.substring(0, stringNumber.length() - 3);
                    bigDecimal = new BigDecimal(resultNumber);
                    return bigDecimal;
                }
            }
        }
        return number;
    }
}
