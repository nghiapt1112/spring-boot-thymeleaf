package com.lyna.web.property;

import com.lyna.web.infrastructure.property.AIProperty;
import com.lyna.web.LynaApplicationTests;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PropertyTest extends LynaApplicationTests {

    @Autowired
    private AIProperty aiProperty;

    @Test
    public void getPropertyAsMap() {
        Assert.assertNotNull(aiProperty);
        Assert.assertNotNull(aiProperty.getHeaders());
        Assert.assertNotNull(aiProperty.getUrl());
    }
}
