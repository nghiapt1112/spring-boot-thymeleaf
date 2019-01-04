package com.lyna.web;

import com.lyna.commons.utils.JsonUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LynaApplicationTests {

    @Test
    public void contextLoads() {
    }

    public void printAsJson(Object o) {
        System.out.println(JsonUtils.toJson(o));
    }

}
