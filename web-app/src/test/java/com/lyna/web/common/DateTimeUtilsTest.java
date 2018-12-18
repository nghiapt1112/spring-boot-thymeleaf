package com.lyna.web.common;

import com.lyna.commons.utils.DateTimeUtils;
import com.lyna.web.LynaApplicationTests;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

public class DateTimeUtilsTest  extends LynaApplicationTests {

    @Test
    public void convertStringToDate() {
        Date dateFromStr = DateTimeUtils.convertStringToDate("2018-11-26 00:00:00");

        Assert.assertNotNull(dateFromStr);
    }
}
