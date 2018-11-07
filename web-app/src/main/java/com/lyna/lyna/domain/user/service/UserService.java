package com.lyna.lyna.domain.user.service;

import com.lyna.lyna.domain.user.User;

public interface UserService {
    User findByEmail(String userEmail);
}
