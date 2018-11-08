package com.lyna.web.domain.user.repository.impl;

import com.lyna.web.domain.user.User;
import com.lyna.web.domain.user.repository.UserRepository;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl implements UserRepository {

    @Override
    public User findByEmail(String email) {
        return null;
    }

}
