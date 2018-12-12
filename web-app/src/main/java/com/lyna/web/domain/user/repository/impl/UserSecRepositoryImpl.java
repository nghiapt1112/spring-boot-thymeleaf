package com.lyna.web.domain.user.repository.impl;

import com.lyna.web.domain.user.User;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class UserSecRepositoryImpl extends SimpleJpaRepository<User, Long> {
    @PersistenceContext
    protected EntityManager entityManager;

    public UserSecRepositoryImpl(EntityManager em) {
        super(User.class, em);
    }

    @SuppressWarnings("unused")
    public User findByEmail(String email) {
        String query = "SELECT u FROM User u inner join fetch u.userStoreAuthorities WHERE u.email = :email";
        return (User) entityManager.createQuery(query).setParameter("email", email).getSingleResult();
    }

}
