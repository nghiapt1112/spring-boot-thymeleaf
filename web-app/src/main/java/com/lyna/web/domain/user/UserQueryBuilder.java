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
        return "SELECT u1.*, usa1.* FROM m_user u1 "
                + " INNER JOIN m_user_store_authority usa1 ON u1.user_id = usa1.user_id "
                + " INNER JOIN m_store s3 ON usa1.store_id = s3.store_id ";
    }

    @Override
    public String buildWhere() {
        Object userName = this.requestPage.getSearchFields().get("search");
        Date startDate = (Date) this.requestPage.getSearchFields().get("start");
        Date endDate = (Date) this.requestPage.getSearchFields().get("end");

        // Non params request
        if (Objects.isNull(userName) && Objects.isNull(startDate) && Objects.isNull(endDate)) {
            return "";
        }

        StringBuilder whereCondition = new StringBuilder(" WHERE ");
        if (Objects.nonNull(userName)) {
            whereCondition.append(" u1.name LIKE :name OR u1.email LIKE :name ");
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
        return " GROUP BY u1.user_id ";
    }

    @Override
    public String buildOrderBy() {
        return "";
    }

    @Override
    public String buildCount() {
        return "SELECT COUNT(DISTINCT(u1.user_id)) FROM m_user u1 "
                + " INNER JOIN m_user_store_authority usa1 ON u1.user_id = usa1.user_id "
                + " INNER JOIN m_store s3 ON usa1.store_id = s3.store_id ";
    }

    /**
     *
     *Return example data: Limit 0,10 (Take 10 items from index 0)
     */
    @Override
    public String buildLimit() {
        int itemsPerPage = this.requestPage.getNoOfRowInPage();
        int currentPage = this.requestPage.getCurrentPage();
        int offset = (currentPage -1)* itemsPerPage;
        return " LIMIT " + offset + "," + itemsPerPage + " ";
    }
}
