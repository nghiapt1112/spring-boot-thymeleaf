package com.lyna.web.domain.user.service.impl;

import com.lyna.commons.infrustructure.service.BaseService;
import com.lyna.web.domain.user.UserStoreAuthority;
import com.lyna.web.domain.user.service.UserStoreAuthorityService;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class UserStoreAuthorityServiceImpl extends BaseService implements UserStoreAuthorityService {

    @Override
    public void assignUserToStore(Collection<UserStoreAuthority> userStoreAuthorities){

    }

}
