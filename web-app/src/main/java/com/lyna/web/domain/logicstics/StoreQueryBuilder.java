package com.lyna.web.domain.logicstics;

import com.lyna.commons.infrustructure.repository.QueryBuilder;

public class StoreQueryBuilder extends QueryBuilder {

    @Override
    public String buildGroupBy() {
        return "";
    }

    @Override
    public String buildOrderBy() {
        return EMPTY_STR;
    }

    @Override
    public String buildWhere() {
        return EMPTY_STR;
    }

    @Override
    public String buildSelect() {
        return "SELECT s.*" + buildFrom();
    }

    @Override
    public String buildCount() {
        return "SELECT COUNT(DISTINCT(o.order_id)) " + buildFrom();
    }

    @Override
    public String buildLimit() {
        return EMPTY_STR;
    }

    @Override
    public String buildFrom() {
        return " FROM m_store AS s " +
                " INNER JOIN m_post_course AS pc ON s.store_id = pc.store_id " +
                " INNER JOIN t_order AS o ON pc.post_course_id = o.post_course_id " +
                " INNER JOIN t_order_detail AS od ON od.order_id = o.order_id " +
                " INNER JOIN m_product AS p ON od.product_id = p.product_id ";
    }
}
