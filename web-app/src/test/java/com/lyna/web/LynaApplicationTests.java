package com.lyna.web;

import com.lyna.commons.infrustructure.base.JsonSerializationException;
import com.lyna.commons.utils.JsonUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LynaApplicationTests {

    @Test
    public void contextLoads() {
    }

    protected void printAsJson(Object o) {
        System.out.println(JsonUtils.toJson(o));
    }

    protected <T> T readFile(String fileName, Class<T> typed) {
        try (InputStream stream = new ClassPathResource(fileName).getInputStream()) {
            return JsonUtils.fromJson(IOUtils.toString(stream, Charset.defaultCharset()), typed);
        } catch (IOException e) {
            e.printStackTrace();
            throw new JsonSerializationException("Error when read file for test.", e);
        }
    }
}
