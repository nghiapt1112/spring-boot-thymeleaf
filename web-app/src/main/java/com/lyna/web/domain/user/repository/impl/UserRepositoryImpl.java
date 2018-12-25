package com.lyna.web.domain.user.repository.impl;

import com.lyna.commons.infrustructure.object.RequestPage;
import com.lyna.web.domain.user.User;
import com.lyna.web.domain.user.UserResponsePage;
import com.lyna.web.domain.user.repository.UserRepository;
import com.lyna.web.infrastructure.repository.BaseRepository;
import com.lyna.web.infrastructure.repository.PagingRepository;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class UserRepositoryImpl extends BaseRepository<User, String> implements UserRepository, PagingRepository {

    public UserRepositoryImpl(EntityManager em) {
        super(User.class, em);
    }

    @Override
    public User findByEmail(String email) {
        String query = "SELECT u FROM User u left join fetch u.userStoreAuthorities left join fetch u.stores WHERE u.email = :email";
        List<User> users = entityManager.createQuery(query, User.class).setParameter("email", email).getResultList();
        return CollectionUtils.isEmpty(users) ? null : users.get(0);
    }

    @Override
    public User findByUserIdAndTenantId(int tenantId, String userId) {
        String query = "SELECT u FROM User u inner join fetch u.userStoreAuthorities inner join fetch u.stores WHERE u.tenantId = :tenantId AND u.id = :id";
        List<User> users = entityManager.createQuery(query, User.class)
                .setParameter("tenantId", tenantId)
                .setParameter("id", userId)
                .getResultList();
        return CollectionUtils.isEmpty(users) ? null : users.get(0);
    }

    @Override
    public Boolean deleteByUserIds(List<String> userIds, int tenantId) {
            String query = "DELETE FROM User u WHERE u.id in (:userIds) AND u.tenantId=:tenantId";
            entityManager.createQuery(query)
                    .setParameter("userIds", userIds)
                    .setParameter("tenantId", tenantId)
                    .executeUpdate();
            return true;
    }

    @Override
    public List<User> findAllByTenantId(int tenantId) {
        TypedQuery<User> query =
                entityManager.createNamedQuery("User.getAll", User.class)
                        .setParameter("tenantId", tenantId);
        return query.getResultList();
    }

    @Override
    public UserResponsePage findUsersWithPaging(RequestPage userRequestPage) {
        return findWithPaging(userRequestPage, UserResponsePage.class);
    }

}
