package com.lyna.web.domain.logicstics;

import com.lyna.commons.infrustructure.object.RequestPage;

import java.util.Objects;

public class LogisticRequestPage extends RequestPage {

    public static final String START_DATE = "start_date";
    public static final String END_DATE = "end_date";
    public static final String POST_NAME = "post_name";

    @Override
    public StringBuilder buildCount() {
        return new StringBuilder("SELECT COUNT(DISTINCT(ordt.order_detail_id)) ").append(buildFrom());
    }

    @Override
    public StringBuilder buildSelect() {
        return new StringBuilder("SELECT v ");
    }

    @Override
    public StringBuilder buildFrom() {
        return new StringBuilder(" FROM LogisticView v ");
    }

    @Override
    public StringBuilder buildGroupBy() {
        return EMPTY_STR;
    }

    @Override
    public StringBuilder buildWhere() {
        StringBuilder whereCondition = new StringBuilder(" WHERE v.tenantId = :tenantId AND (v.orderDate BETWEEN :start_date AND :end_date) ");
        params.put("tenantId", this.tenantId);
        params.put(END_DATE, searchFields.get(END_DATE));
        params.put(START_DATE, searchFields.get(START_DATE));

        if (Objects.nonNull(searchFields.get(POST_NAME))) {
            whereCondition.append(" AND v.post like :post_name ");
            params.put(POST_NAME, "%" + searchFields.get(POST_NAME) + "%");
        }
        return whereCondition;
    }


    @Override
    public StringBuilder buildOrderBy() {
        return EMPTY_STR;
    }

    @Override
    public StringBuilder buildLimit() {
        return EMPTY_STR;
    }

}
