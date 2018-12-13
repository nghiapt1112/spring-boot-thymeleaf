package com.lyna.web.domain.logicstics;

import com.lyna.commons.infrustructure.object.RequestPage;

public class LogisticRequestPage extends RequestPage {

    @Override
    public StringBuilder buildCount() {
        return new StringBuilder("SELECT COUNT(DISTINCT(ordt.order_detail_id)) ").append(buildFrom());
    }

    @Override
    public StringBuilder buildSelect() {
        return new StringBuilder(" SELECT ")
                .append(" stor.store_id        AS  storeId,")
                .append(" stor.name            AS  storeName,")
                .append(" ordr.order_date      AS  orderDate,")
                .append(" ordr.order_id        AS  orderId,")
                .append(" count(ordr.order_id) AS  totalOrder,")

                .append(" pstc.post            AS  postName,")
                .append(" pstc.course          AS  courseName,")
                .append(" SUM(prdc.price)      AS  amount,")
                .append(" dlvr.delivery_id     AS  deliveryId, ")
                .append(" lgst.logistics_id    AS  logisticId ")
                ;
    }

    @Override
    public StringBuilder buildFrom() {
        return new StringBuilder(" FROM ")
                .append("( ")
                .append(" t_order AS ordr ")
                .append(" INNER JOIN t_order_detail      AS ordt  ON ordt.order_id        =  ordr.order_id ")
                .append(" INNER JOIN m_product           AS prdc  ON ordt.product_id      =  prdc.product_id ")
                .append(" INNER JOIN m_post_course       AS pstc  ON ordr.post_course_id  =  pstc.post_course_id ")
                .append(" INNER JOIN m_store             AS stor  ON pstc.store_id        =  stor.store_id ")
//  logisticView
                .append(" INNER JOIN t_logistics         AS lgst  ON lgst.order_id        =  ordr.order_id ")
                .append(" INNER JOIN t_logistics_detail  AS lgdt  ON lgdt.logistics_id    =  lgst.logistics_id ")
                .append(" INNER JOIN m_package           AS pckg  ON pckg.pakage_id       =  lgdt.package_id ")
                .append(") ")
//  deliveryView
                .append(" LEFT JOIN t_delivery           AS dlvr  ON dlvr.order_id        = ordr.order_id ");
    }


    @Override
    public StringBuilder buildWhere() {
        return EMPTY_STR;
    }

    @Override
    public StringBuilder buildGroupBy() {
//        return new StringBuilder(" GROUP BY ")
//                .append(" ordr.order_date, ")
//                .append(" pstc.post_course_id, ")
//                .append(" stor.store_id ");

        return new StringBuilder(" GROUP BY ")
                .append(" stor.store_id , ")
                .append(" pstc.post_course_id, ")
                .append(" ordr.order_id");

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
