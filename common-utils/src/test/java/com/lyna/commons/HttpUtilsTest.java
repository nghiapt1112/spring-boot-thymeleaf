package com.lyna.commons;

import com.lyna.commons.infrustructure.exception.ResourceException;
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
        httpGet = "http://localhost:8080";
        httpsGet = "https://www.google.com/";
    }

    @Test(expected = ResourceException.class)
    public void testHttpGet() {
        HttpUtils.httpGet(httpGet, String.class);
    }

    @Test
    public void testHttpsGet() {
        String body = HttpUtils.httpsGet(httpsGet, null);
        Assert.assertNotNull(body);
    }

    @After
    public void release() {
        this.httpGet = null;
        this.httpsGet = null;
    }
}
