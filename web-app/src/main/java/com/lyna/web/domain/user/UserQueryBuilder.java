package com.lyna.web.domain.user;

import com.lyna.web.infrastructure.repository.QueryBuilder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserQueryBuilder extends QueryBuilder {

    public static final String USER_QUERY_ALIAS = "u1";

    @Override
    public String buildGroupBy() {
        return "GROUP BY";
    }

    @Override
    public String buildOrderBy() {
        return "ORDER BY";
    }

    @Override
    public String buildWhere() {
        return "WHERE ";
    }
}
