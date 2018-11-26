package com.lyna.web.domain.user;

import com.lyna.commons.infrustructure.repository.QueryBuilder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Objects;

@Data
@NoArgsConstructor
public class UserQueryBuilder extends QueryBuilder {

    @Override
    public String buildSelect() {
        return "SELECT u1 FROM User u1 join fetch u1.userStoreAuthorities inner join fetch u1.stores";
    }

    @Override
    public String buildWhere() {
        Object userName = this.requestPage.getSearchFields().get("name");
        Date startDate = (Date) this.requestPage.getSearchFields().get("start");
        Date endDate = (Date) this.requestPage.getSearchFields().get("end");

        // Non params request
        if (Objects.isNull(userName) && Objects.isNull(startDate) && Objects.isNull(endDate)) {
            return "";
        }

        StringBuilder whereCondition = new StringBuilder(" WHERE ");
        if (Objects.nonNull(userName)) {
            whereCondition.append("u1.name LIKE :name ");
            this.params.put("name", "%" + userName.toString().trim() + "%");
        }
        if (Objects.nonNull(startDate) && Objects.nonNull(endDate)) {
            whereCondition.append(" AND u1.createDate BETWEEN :start AND :end ");
            this.params.put("start", startDate);
            this.params.put("end", endDate);
        }

        return whereCondition.toString();
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

    @Override
    public String buildLimit() {
        return "LIMIT 0,2";
    }
}
