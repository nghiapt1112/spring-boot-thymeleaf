package com.lyna.lyna.domain.user.repository;

import com.lyna.lyna.domain.user.User;

public interface UserRepository {
    User findByEmail(String email);
}
