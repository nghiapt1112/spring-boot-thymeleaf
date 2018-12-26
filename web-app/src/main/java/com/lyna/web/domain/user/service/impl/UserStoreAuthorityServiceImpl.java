package com.lyna.web.domain.user.service.impl;

import com.lyna.commons.infrustructure.service.BaseService;
import com.lyna.commons.utils.Constants;
import com.lyna.commons.utils.DataUtils;
import com.lyna.web.domain.user.UserStoreAuthority;
import com.lyna.web.domain.user.exception.UserException;
import com.lyna.web.domain.user.repository.UserRepository;
import com.lyna.web.domain.user.repository.UserStoreAuthorityRepository;
import com.lyna.web.domain.user.service.UserStoreAuthorityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

@Service
public class UserStoreAuthorityServiceImpl extends BaseService implements UserStoreAuthorityService {

    private final Logger log = LoggerFactory.getLogger(UserStoreAuthorityServiceImpl.class);

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
            log.error(e.getMessage());
            throw new UserException(toInteger("err.user.deleteFailed.code"), toStr("err.user.deleteFailed.msg"));
        }

    }


}
