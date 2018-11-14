package com.lyna.web.domain.user.service.impl;

import com.lyna.web.domain.user.repository.impl.UserSecRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserSecRepositoryImpl userSecRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Finding user with" + username);

        //  TODO: 1. Nghia.Pham using service instead of repository,
        //  TODO: 2. handle exception when fall in EntityNotFoundException.
        return userSecRepository.findByEmail(username);
    }


}
