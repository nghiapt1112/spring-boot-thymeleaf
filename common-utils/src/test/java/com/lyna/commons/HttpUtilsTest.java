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

    @Test
    public void testPost() {
        String json = "{  \n" +
                "   \"trainingDatas\":[  \n" +
                "      {  \n" +
                "         \"inputItemNums\":[  \n" +
                "            2,\n" +
                "            2\n" +
                "         ],\n" +
                "         \"outputItemNums\":[  \n" +
                "            3,\n" +
                "            3\n" +
                "         ]\n" +
                "      }\n" +
                "   ],\n" +
                "   \"unknownDatas\":[  \n" +
                "      {  \n" +
                "         \"inputItemNums\":[  \n" +
                "            2,\n" +
                "            2\n" +
                "         ]\n" +
                "      }\n" +
                "   ]\n" +
                "}";
        System.out.println(json);
        String url = "https://jp6jra2fwj.execute-api.ap-northeast-1.amazonaws.com/test/test3";
        Object res = HttpUtils.httpsPost(url, json, null);
        System.out.println(res);
    }

    @After
    public void release() {
        this.httpGet = null;
        this.httpsGet = null;
    }
}
