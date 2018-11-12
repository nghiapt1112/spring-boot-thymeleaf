package com.lyna.security;

import com.lyna.security.domain.Roles;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class SecurityUtils {
    private static final String ROLE_PREFIX = "ROLE_";

    public static Set<GrantedAuthority> generateGrantedAuthorities(Collection<Roles> roles) {
        Set<GrantedAuthority> grantedAuthorities = roles
                .stream().map(r -> new SimpleGrantedAuthority(ROLE_PREFIX.concat(r.getRoleNameUpperCase())))
                .collect(Collectors.toSet());
        return grantedAuthorities;
    }
}
