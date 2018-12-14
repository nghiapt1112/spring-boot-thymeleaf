package com.lyna.web.domain.order;

import com.lyna.commons.infrustructure.object.RequestPage;

import java.util.Objects;

public class OrderRequestPage extends RequestPage {

    public static final String START_DATE = "start_date";
    public static final String END_DATE = "end_date";
    public static final String POST_NAME = "post_name";

    @Override
    public StringBuilder buildSelect() {
        return new StringBuilder("SELECT o ");
    }

    @Override
    public StringBuilder buildFrom() {
        return new StringBuilder(" FROM OrderView o ");
    }

    @Override
    public StringBuilder buildWhere() {
        StringBuilder whereCondition = new StringBuilder(" WHERE o.tenantId = :tenantId AND (o.orderDate BETWEEN :start_date AND :end_date) ");
        params.put("tenantId", this.tenantId);
        params.put(END_DATE, searchFields.get(END_DATE));
        params.put(START_DATE, searchFields.get(START_DATE));

        if (Objects.nonNull(searchFields.get(POST_NAME))) {
            whereCondition.append(" AND o.postName like :post_name ");
            params.put(POST_NAME, "%" + searchFields.get(POST_NAME) + "%");
        }
        return whereCondition;
    }

    @Override
    public StringBuilder buildGroupBy() {
        return null;
    }

    @Override
    public StringBuilder buildOrderBy() {
        return null;
    }

    @Override
    public StringBuilder buildCount() {
        return null;
    }


    @Override
    public StringBuilder buildLimit() {
        return null;
    }


}
