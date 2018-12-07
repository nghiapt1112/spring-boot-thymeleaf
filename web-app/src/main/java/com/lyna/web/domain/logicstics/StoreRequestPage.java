package com.lyna.web.domain.logicstics;

import com.lyna.commons.infrustructure.object.RequestPage;

public class StoreRequestPage extends RequestPage {

    @Override
    public StringBuilder buildGroupBy() {
        return EMPTY_STR;
    }

    @Override
    public StringBuilder buildOrderBy() {
        return EMPTY_STR;
    }

    @Override
    public StringBuilder buildWhere() {
        this.params.put("tenantId", this.tenantId);
        return new StringBuilder(" WHERE ")
                .append("o.tenant_id = :tenantId ")
                .append("AND od.tenant_id = :tenantId ")
                .append("AND s.tenant_id = :tenantId ")
                .append("AND p.tenant_id = :tenantId ")
                .append("AND od.tenant_id = :tenantId ")
                .append("AND pc.tenant_id = :tenantId ");
    }

    @Override
    public StringBuilder buildSelect() {
        return new StringBuilder(" SELECT ")
                .append(" o.create_date AS orderDate, ")
                .append(" o.order_id, ")
                .append(" od.order_detail_id, ")
                .append(" od.product_id, ")
                .append(" od.amount AS amount, ")
                .append(" s.name AS storeName, ")
                .append(" pc.post as postName, ")
                .append(" p.name AS productName, ")
                .append(" p.price AS productPrice, ")
                .append(" p.category1 AS firstCategory, ")
                .append(" p.category2 AS secondCategory, ")
                .append(" p.category3 AS thirdCategory ")
                .append(buildFrom());
    }

    @Override
    public StringBuilder buildCount() {
        return new StringBuilder("SELECT COUNT(o.order_id) ").append(buildFrom());
    }

    @Override
    public StringBuilder buildLimit() {
        return EMPTY_STR;
    }

    @Override
    public StringBuilder buildFrom() {
        return new StringBuilder(" FROM m_store AS s ")
                .append(" INNER JOIN m_post_course AS pc ON s.store_id = pc.store_id ")
                .append(" INNER JOIN t_order AS o ON pc.post_course_id = o.post_course_id ")
                .append(" INNER JOIN t_order_detail AS od ON od.order_id = o.order_id ")
                .append(" INNER JOIN m_product AS p ON od.product_id = p.product_id ");
    }

}
