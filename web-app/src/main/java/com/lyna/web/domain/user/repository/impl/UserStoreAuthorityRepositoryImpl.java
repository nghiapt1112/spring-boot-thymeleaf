package com.lyna.web.domain.user.repository.impl;

import com.lyna.web.domain.user.UserStoreAuthority;
import com.lyna.web.domain.user.repository.UserStoreAuthorityRepository;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Collection;

@Repository
public class UserStoreAuthorityRepositoryImpl extends SimpleJpaRepository<UserStoreAuthority, String> implements UserStoreAuthorityRepository {

    public UserStoreAuthorityRepositoryImpl(EntityManager em) {
        super(UserStoreAuthority.class, em);
    }

    @Override
    public void assignUserToStore(Collection<UserStoreAuthority> userStoreAuthorities) {

    }
}
