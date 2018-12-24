package com.lyna.commons;

import com.lyna.commons.utils.HttpUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class HttpUtilsTest {

    private String httpGet;
    private String httpsGet;

    @Before
    public void init() {
        httpGet = "http://nghia.lyna.com/user/test";
        httpsGet = "https://www.adayroi.com/";
    }

    @Test
    public void testHttpGet() {
        String body = HttpUtils.httpGet(httpGet, String.class);
        Assert.assertEquals("s", body);
    }

    @Test
    public void testHttpsGet() {
        String body = HttpUtils.httpsGet(httpGet, null);
        Assert.assertNotNull(body);
    }


    @After
    public void release() {
        this.httpGet = null;
        this.httpsGet = null;
    }
}
