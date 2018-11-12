package com.lyna.security.domain;

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
        // TODO: using service instead of repository, handle exception when fall in EntityNotFoundException.
        User user = userSecRepository.findByEmail(username);

//        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), user.isEnable(), true,
//                true, true, this.generateGrantedAuthorities(user.getRoles()));
        return user;
    }


}
