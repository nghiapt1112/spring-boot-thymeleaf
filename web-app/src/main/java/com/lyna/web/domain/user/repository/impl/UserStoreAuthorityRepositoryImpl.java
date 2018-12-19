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
    public Boolean deleteUserStoreAuthorityByUserIds(List<String> userIds, int tenantId) {
            String query = "DELETE FROM UserStoreAuthority u WHERE u.userId in (:userId) AND u.tenantId=:tenantId";
            entityManager.createQuery(query)
                    .setParameter("userId", userIds)
                    .setParameter("tenantId", tenantId)
                    .executeUpdate();
            return true;
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
