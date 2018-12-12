package com.lyna.web.domain.user.service;

import com.lyna.web.domain.user.UserStoreAuthority;

import java.util.Collection;

public interface UserStoreAuthorityService {
    void assignUserToStore(Collection<UserStoreAuthority> userStoreAuthorities);
}

