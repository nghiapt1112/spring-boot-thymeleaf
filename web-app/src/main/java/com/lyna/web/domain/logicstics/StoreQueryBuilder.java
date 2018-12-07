//package com.lyna.web.domain.logicstics;
//
//import com.lyna.commons.infrustructure.repository.QueryBuilder;
//
//public class StoreQueryBuilder extends QueryBuilder {
//
//    @Override
//    public String buildGroupBy() {
//        return "";
//    }
//
//    @Override
//    public String buildOrderBy() {
//        return EMPTY_STR;
//    }
//
//    @Override
//    public String buildWhere() {
//        this.params.put("tenantId", this.requestPage.getTenantId());
//        return new StringBuilder(" WHERE ")
//                .append("o.tenant_id = :tenantId ")
//                .append("AND od.tenant_id = :tenantId ")
//                .append("AND s.tenant_id = :tenantId ")
//                .append("AND p.tenant_id = :tenantId ")
//                .append("AND od.tenant_id = :tenantId ")
//                .append("AND pc.tenant_id = :tenantId ")
//                .toString();
//    }
//
//    @Override
//    public String buildSelect() {
//        return new StringBuilder(" SELECT ")
//                .append(" o.create_date AS orderDate, ")
//                .append(" o.order_id, ")
//                .append(" od.order_detail_id, ")
//                .append(" od.product_id, ")
//                .append(" od.amount AS amount, ")
//                .append(" s.name AS storeName, ")
//                .append(" pc.post as postName, ")
//                .append(" p.name AS productName, ")
//                .append(" p.price AS productPrice, ")
//                .append(" p.category1 AS firstCategory, ")
//                .append(" p.category2 AS secondCategory, ")
//                .append(" p.category3 AS thirdCategory ")
//                .append(buildFrom())
//                .toString();
//    }
//
//    @Override
//    public String buildCount() {
//        return "SELECT COUNT(o.order_id) " + buildFrom();
//    }
//
//    @Override
//    public String buildLimit() {
//        return EMPTY_STR;
//    }
//
//    @Override
//    public String buildFrom() {
//        return " FROM m_store AS s " +
//                " INNER JOIN m_post_course AS pc ON s.store_id = pc.store_id " +
//                " INNER JOIN t_order AS o ON pc.post_course_id = o.post_course_id " +
//                " INNER JOIN t_order_detail AS od ON od.order_id = o.order_id " +
//                " INNER JOIN m_product AS p ON od.product_id = p.product_id ";
//    }
//}
