package com.lyna.web.domain.user.repository.impl;

import com.lyna.commons.infrustructure.repository.BaseRepository;
import com.lyna.web.domain.delivery.DeliveryDetail;
import com.lyna.web.domain.user.User;
import com.lyna.web.domain.user.repository.UserRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class UserRepositoryImpl extends BaseRepository<User, Long> implements UserRepository {

    public UserRepositoryImpl(EntityManager em) {
        super(User.class, em);
    }

    @Override
    public User findByEmail(String email) {
        String query = "SELECT u FROM User u inner join fetch u.userStoreAuthorities WHERE u.email = :email";
        return (User) entityManager.createQuery(query).setParameter("email", email).getSingleResult();
    }

}
