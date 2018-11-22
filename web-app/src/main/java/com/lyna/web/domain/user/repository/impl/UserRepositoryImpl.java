package com.lyna.web.domain.user.repository.impl;

import com.lyna.commons.infrustructure.repository.BaseRepository;
import com.lyna.web.domain.user.User;
import com.lyna.web.domain.user.repository.UserRepository;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class UserRepositoryImpl extends BaseRepository<User, String> implements UserRepository {

    public UserRepositoryImpl(EntityManager em) {
        super(User.class, em);
    }

    @Override
    public User findByEmail(String email) {
        String query = "SELECT u FROM User u inner join fetch u.userStoreAuthorities inner join fetch u.stores WHERE u.email = :email";
        List<User> users = entityManager.createQuery(query, User.class).setParameter("email", email).getResultList();
        return CollectionUtils.isEmpty(users) ? null : users.get(0);
    }

    @Override
    public User findById(int tenantId, String userId) {
        String query = "SELECT u FROM User u inner join fetch u.userStoreAuthorities inner join fetch u.stores WHERE u.tenantId = :tenantId AND u.id = :id";
        List<User> users = entityManager.createQuery(query, User.class)
                .setParameter("tenantId", tenantId)
                .setParameter("id", userId)
                .getResultList();
        return CollectionUtils.isEmpty(users) ? null : users.get(0);
    }

 	@Override
    @Transactional
    public Boolean deleteByUserId(List<String> sUserId) {
        try {
            String query = "DELETE FROM User u WHERE u.id in (:usertId)";
            int deletedCount = entityManager.createQuery(query).setParameter("usertId", sUserId).executeUpdate();
            return true;
        } catch (Exception ex) {
        }
        return false;
    }

    @Override
    public void commitTransaction() {
        //TODO: Rollback data when delete user fail: for sprint 2
    }
}
