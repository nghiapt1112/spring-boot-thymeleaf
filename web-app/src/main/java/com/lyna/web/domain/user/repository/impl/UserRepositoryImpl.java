package com.lyna.web.domain.user.repository.impl;

import com.lyna.commons.infrustructure.repository.BaseRepository;
import com.lyna.web.domain.user.User;
import com.lyna.web.domain.user.repository.UserRepository;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class UserRepositoryImpl extends BaseRepository<User, Long> implements UserRepository {

    public UserRepositoryImpl(EntityManager em) {
        super(User.class, em);
    }

    @Override
    public User findByEmail(String email) {
        String query = "SELECT u FROM User u inner join fetch u.userStoreAuthorities WHERE u.email = :email";
        List<User> users = entityManager.createQuery(query, User.class).setParameter("email", email).getResultList();
        return CollectionUtils.isEmpty(users) ? null: users.get(0);
    }

}
