package com.lyna.lyna.domain.user.service.impl;

import com.lyna.lyna.domain.user.User;
import com.lyna.lyna.domain.user.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public User findByEmail(String userEmail) {
        return null;
    }
}
