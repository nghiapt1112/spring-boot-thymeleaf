package com.lyna.web.domain.logicstics;

import com.lyna.commons.infrustructure.object.RequestPage;

public class LogisticRequestPage extends RequestPage {


    @Override
    public StringBuilder buildGroupBy() {
        return new StringBuilder(" GROUP BY ")
                .append(" ordr.order_id, ")
                .append(" lgst.order_id, ")
                .append(" lgdt.logistics_id ");
    }

    @Override
    public StringBuilder buildOrderBy() {
        return EMPTY_STR;
    }

    @Override
    public StringBuilder buildWhere() {
        return EMPTY_STR;
    }

    @Override
    public StringBuilder buildSelect() {
        return new StringBuilder(" SELECT ")
                .append(" stor.store_id ,")
                .append(" ordr.order_id        AS  order_idhihi ,")
                .append(" ordt.order_id        AS  orderDetail_orderId ,")
                .append(" count(ordr.order_id) AS  total_order ,")
                .append(" stor.`name`          AS  store_name ,")
                .append(" pstc.post            AS  post,")
                .append(" pstc.course          AS  course,")
                .append(" SUM(prdc.price)      AS  total_price ,")
//  Logistic 's fields
                .append(" pckg.`name`          AS  logigsticPackageName ,")
                .append(" lgdt.amount          AS  logisticAmount ")
//  Delivery 's fields
                ;
    }

    @Override
    public StringBuilder buildCount() {
        return EMPTY_STR;
    }

    @Override
    public StringBuilder buildLimit() {
        return EMPTY_STR;
    }

    @Override
    public StringBuilder buildFrom() {
        return new StringBuilder(" FROM ")
                .append(" t_order AS ordr ")
                .append(" INNER JOIN t_order_detail      AS ordt  ON ordt.order_id        =  ordr.order_id ")
                .append(" INNER JOIN m_product           AS prdc  ON ordt.product_id      =  prdc.product_id ")
                .append(" INNER JOIN m_post_course       AS pstc  ON ordr.post_course_id  =  pstc.post_course_id ")
                .append(" INNER JOIN m_store             AS stor  ON pstc.store_id        =  stor.store_id ")
//   mapping for logisticView
                .append(" INNER JOIN t_logistics         AS lgst  ON lgst.order_id        =  ordr.order_id ")
                .append(" INNER JOIN t_logistics_detail  AS lgdt  ON lgdt.logistics_id    =  lgst.logistics_id ")
                .append(" INNER JOIN m_package           AS pckg  ON pckg.pakage_id       =  lgdt.package_id ")
//  mapping for delivery
//                .append(" -- INNER JOIN t_delivery AS dl2 ON dl2.order_id = ordr.order_id ")
//                .append(" -- INNER JOIN t_delivery_detail AS dldt2 ON dldt2.delivery_id = dl2.delivery_id ")
//                .append(" -- INNER JOIN m_package AS pk2 ON pk2.pakage_id = dldt2.package_id ");
                ;
    }

}
