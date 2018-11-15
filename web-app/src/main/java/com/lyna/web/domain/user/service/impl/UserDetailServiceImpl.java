package com.lyna.web.domain.user.service.impl;

import com.lyna.commons.infrustructure.service.BaseService;
import com.lyna.web.domain.user.User;
import com.lyna.web.domain.user.exception.UserException;
import com.lyna.web.domain.user.repository.impl.UserSecRepositoryImpl;
import com.lyna.web.domain.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserDetailServiceImpl extends BaseService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Finding user with" + username);
        User user = userService.findByEmail(username);
        if (Objects.isNull(user)) {
            //  TODO: throw authenticate Exception cause by: User not existed.
            throw new UserException(toInteger(""), toStr(""));
        } else {
            return user;
        }
    }


}
