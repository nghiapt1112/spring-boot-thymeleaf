package com.lyna.web.security;

import com.lyna.web.security.domain.UserStoreAuthority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.security.Principal;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public class SecurityUtils {
    private static final String ROLE_PREFIX = "ROLE_";

    public static Set<GrantedAuthority> populateGrantedAuthorities(Collection<UserStoreAuthority> authorities) {
        Set<GrantedAuthority> grantedAuthorities = authorities.stream()
                .map(r -> new SimpleGrantedAuthority(ROLE_PREFIX.concat(r.storeAuthorityToUserAuthority())))
                .collect(Collectors.toSet());
        return grantedAuthorities;
    }

    public String getCurrentTenant(Principal principal) {
        return null;
    }
}
