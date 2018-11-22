package com.lyna.web.domain.user.repository.impl;

import com.lyna.commons.infrustructure.object.AbstractEntity;
import com.lyna.commons.infrustructure.repository.BaseRepository;
import com.lyna.web.domain.user.User;
import com.lyna.web.domain.user.UserQueryBuilder;
import com.lyna.web.domain.user.UserResponsePage;
import com.lyna.web.domain.user.repository.UserRepository;
import com.lyna.web.infrastructure.object.RequestPage;
import com.lyna.web.infrastructure.repository.QueryBuilder;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
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
    public UserResponsePage findUserWithPaging(RequestPage userRequestPage) {
        QueryBuilder queryBuilder = new UserQueryBuilder();
        String whereCondition = queryBuilder.buildWhere();

        StringBuilder query = new StringBuilder("SELECT * ");
        query.append("FROM USER " + UserQueryBuilder.USER_QUERY_ALIAS + "");
        query.append(whereCondition);

        Map<String, Object> params = new HashMap<>();

        TypedQuery tQuery = entityManager.createQuery(query.toString(), AbstractEntity.class);

        // To return totoal page for FE => 2 queries command: countAllInTAble(condition.withoutGroupBy) + findWithOffset(condition)
        long totalRerords = this.countTotalRecord("query.withoutGroupBy", params);

        List<User> users = tQuery.getResultList();

        return new UserResponsePage(userRequestPage.getNoOfRowInPage(), users, totalRerords);
    }

    //    TODO: =>NghiaPT move to common functions.
    private long countTotalRecord(String s, Map<String, Object> params) {
        return 0;
    }
}
