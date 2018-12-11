package com.lyna.web.domain.user.repository.impl;

import com.lyna.commons.infrustructure.exception.DomainException;
import com.lyna.commons.infrustructure.repository.BaseRepository;
import com.lyna.web.domain.user.UserStoreAuthority;
import com.lyna.web.domain.user.repository.UserStoreAuthorityRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
public class UserStoreAuthorityRepositoryImpl extends BaseRepository<UserStoreAuthority, String> implements UserStoreAuthorityRepository {

    private final Logger log = LoggerFactory.getLogger(UserStoreAuthorityRepositoryImpl.class);

    public UserStoreAuthorityRepositoryImpl(EntityManager em) {
        super(UserStoreAuthority.class, em);
    }

    @Override
    public Boolean deleteUserStoreAuthorityByUserId(List<String> userIds) {
        try {
            String query = "DELETE FROM UserStoreAuthority u WHERE u.userId in (:userId)";
            entityManager.createQuery(query)
                    .setParameter("userId", userIds).executeUpdate();
            return true;
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return false;
    }

    @Override
    public boolean deleteStoreAuthorityByStoreId(List<String> listStoreId) throws DomainException {
        try {
            String query = "DELETE FROM UserStoreAuthority u WHERE u.storeId in (:storeIds)";
            entityManager.createQuery(query)
                    .setParameter("storeIds", listStoreId).executeUpdate();
            return true;
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
        }
        return true;
    }
}
