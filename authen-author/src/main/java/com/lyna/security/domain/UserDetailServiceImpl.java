package com.lyna.security.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    private static final String ROLE_PREFIX = "ROLE_";
    @Autowired
    private UserSecRepositoryImpl userSecRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Finding user with" + username);
        // TODO: using service instead of repository, handle exception when fall in EntityNotFoundException.
        User user = userSecRepository.findByEmail(username);

        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), user.isEnable(), true,
                true, true, this.generateGrantedAuthorities(user.getRoles()));
    }


    public Set<GrantedAuthority> generateGrantedAuthorities(Collection<Roles> roles) {
        Set<GrantedAuthority> grantedAuthorities = roles
                .stream().map(r -> new SimpleGrantedAuthority(ROLE_PREFIX.concat(r.getRoleNameUpperCase())))
                .collect(Collectors.toSet());
        return grantedAuthorities;
    }
}
