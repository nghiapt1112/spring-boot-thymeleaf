package com.lyna.web.domain.user.service.impl;

import com.lyna.web.domain.user.User;
import com.lyna.web.domain.user.repository.UserRepository;
import com.lyna.web.domain.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User findByEmail(String userEmail) {
        return userRepository.findByEmail(userEmail);
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User registerUser(User user) {
        User newUser = this.createUser(user);
        return newUser;
    }

    // Assumsion that: we already validate Input from
    private void validateUser(User user) {

    }

}
