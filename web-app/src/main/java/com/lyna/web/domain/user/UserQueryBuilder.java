package com.lyna.web.domain.user;

import com.lyna.commons.infrustructure.repository.QueryBuilder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserQueryBuilder extends QueryBuilder {

    @Override
    public String buildSelect() {
        return "SELECT u1 FROM User u1 inner join fetch u1.userStoreAuthorities inner join fetch u1.stores";
    }

    @Override
    public String buildWhere() {
        return " WHERE u1.name LIKE '%nghia%' AND u1.createDate>'2017-11-21 13:54:37' ";
    }

    @Override
    public String buildGroupBy() {
        return "GROUP BY";
    }

    @Override
    public String buildOrderBy() {
        return " ORDER BY u1.name DESC";
    }

    @Override
    public String buildCount() {
        return "SELECT count(DISTINCT u1.id ) FROM User u1 ";
    }
}
