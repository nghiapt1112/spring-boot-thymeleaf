package com.lyna.web.user;

import com.lyna.web.LynaApplicationTests;
import com.lyna.web.domain.user.User;
import com.lyna.web.domain.user.service.UserService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserServiceTest extends LynaApplicationTests {

    @Autowired
    private UserService userService;


    //@Test
    public void findByName() {
        User user = userService.findByEmail("admin@tenant1.com");
        System.out.println(user);
    }


    @Test
    public void findById() {
        String requestUserId = "507f191e810c19729de860ea";
        User user = userService.findByUserIdAndTenantId(1, requestUserId);
        Assert.assertNotNull(user);
        Assert.assertEquals(1, user.getTenantId());
        Assert.assertEquals(requestUserId, user.getId());

    }
}
