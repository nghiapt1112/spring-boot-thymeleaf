package com.lyna.web.security;

import com.lyna.web.domain.user.User;
import com.lyna.web.domain.user.UserStoreAuthority;
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

    // TODO: =>Nghia.Pham implement getCurrentTenant for currentAuthenticated user.
    public int getCurrentTenant(Principal principal) {
        User userPrincipal = (User) principal;
        return userPrincipal.getTenantId();
    }

    public User getCurrentAuthenticatedUser(Principal principal) {
        return (User) principal;
    }
}
