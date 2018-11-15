package com.lyna.web.domain.user.service;

import com.lyna.web.domain.user.User;

public interface UserService {
    User findByEmail(String userEmail);

    User registerUser(User user);

    User createUser(User user);
}
