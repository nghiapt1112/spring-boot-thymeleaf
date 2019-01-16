package com.lyna.web.domain.user.service.impl;

import com.lyna.commons.infrustructure.exception.DomainException;
import com.lyna.commons.infrustructure.service.BaseService;
import com.lyna.web.domain.user.UserStoreAuthority;
import com.lyna.web.domain.user.repository.UserRepository;
import com.lyna.web.domain.user.repository.UserStoreAuthorityRepository;
import com.lyna.web.domain.user.service.UserStoreAuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Service
public class UserStoreAuthorityServiceImpl extends BaseService implements UserStoreAuthorityService {

    @Autowired
    private UserStoreAuthorityRepository userStoreAuthorityRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void assignUserToStore(Collection<UserStoreAuthority> userStoreAuthorities) {
        userStoreAuthorityRepository.saveAll(userStoreAuthorities);
    }

    @Override
    @Transactional
    public boolean deleteUserStoreAuthorityByUserIds(List<String> userIds, int tenantId) {
        try {
            userStoreAuthorityRepository.deleteUserStoreAuthorityByUserIds(userIds, tenantId);
            userRepository.deleteByUserIds(userIds, tenantId);
            return true;
        } catch (Exception e) {
            throw new DomainException(toInteger("err.general.deleteFailed.code"), toStr("err.general.deleteFailed.msg"));
        }
    }

}
