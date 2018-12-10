package com.lyna.web.domain.user;

import com.lyna.commons.infrustructure.object.RequestPage;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Objects;

@NoArgsConstructor
public class UserRequestPage extends RequestPage {


    public StringBuilder buildFrom() {
        return new StringBuilder("FROM m_user u1 ")
                .append(" INNER JOIN m_user_store_authority usa1 ON u1.user_id = usa1.user_id ")
                .append(" INNER JOIN m_store s3 ON usa1.store_id = s3.store_id ");
    }

    @Override
    public StringBuilder buildSelect() {
        return new StringBuilder("SELECT u1.*, usa1.* ").append(buildFrom());

    }

    @Override
    public StringBuilder buildWhere() {
        Object userName = this.searchFields.get("search");
        Date startDate = (Date) this.searchFields.get("start");
        Date endDate = (Date) this.searchFields.get("end");

        // Non params request
        if (Objects.isNull(userName) && Objects.isNull(startDate) && Objects.isNull(endDate)) {
            return EMPTY_STR;
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

        return whereCondition;
    }

    @Override
    public StringBuilder buildGroupBy() {
        return new StringBuilder(" GROUP BY u1.user_id ");
    }

    @Override
    public StringBuilder buildOrderBy() {
        return EMPTY_STR;
    }

    @Override
    public StringBuilder buildCount() {
        return new StringBuilder("SELECT COUNT(DISTINCT(u1.user_id)) ").append(buildFrom());
    }

    /**
     * Return example data: Limit 0,10 (Take 10 items from index 0)
     */
    @Override
    public StringBuilder buildLimit() {
        int itemsPerPage = this.noOfRowInPage;
        int currentPage = this.currentPage;
        int offset = (currentPage - 1) * itemsPerPage;
        return new StringBuilder(" LIMIT " + offset + "," + itemsPerPage + " ");
    }

}