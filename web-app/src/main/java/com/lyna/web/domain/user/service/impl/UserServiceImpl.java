package com.lyna.web.domain.user.service.impl;

import com.lyna.web.domain.user.User;
import com.lyna.web.domain.user.service.UserService;
import org.springframework.stereotype.Service;

//@Service
public class UserServiceImpl implements UserService {

    @Override
    public User findByEmail(String userEmail) {
        System.out.println("Finding userEmail" + userEmail);
        return null;
    }
}
