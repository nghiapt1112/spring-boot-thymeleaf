package com.lyna.web.domain.user.service;

import com.lyna.web.domain.user.UserStoreAuthority;

import java.util.Collection;
import java.util.List;

public interface UserStoreAuthorityService {
    void assignUserToStore(Collection<UserStoreAuthority> userStoreAuthorities);

    boolean deleteUserStoreAuthorityByUserIds(List<String> userIds, int tenantId);
}

