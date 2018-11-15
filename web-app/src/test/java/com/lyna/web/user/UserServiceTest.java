package com.lyna.web.user;

import com.lyna.web.LynaApplicationTests;
import com.lyna.web.domain.user.User;
import com.lyna.web.domain.user.service.UserService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserServiceTest extends LynaApplicationTests {

    @Autowired
    private UserService userService;


    @Test
    public void findByName() {
        User user = userService.findByEmail("admin@tenant1.com");
        System.out.println(user);
    }
}
