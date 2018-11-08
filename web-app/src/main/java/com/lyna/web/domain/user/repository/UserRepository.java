package com.lyna.web.domain.user.repository;

import com.lyna.web.domain.user.User;

public interface UserRepository {
    User findByEmail(String email);
}
