package com.lyna.web.domain.logicstics;

import com.lyna.commons.infrustructure.object.RequestPage;

public class LogisticRequestPage extends RequestPage {


    @Override
    public StringBuilder buildGroupBy() {
        return new StringBuilder(" GROUP BY ")
                .append(" ordr.order_id, ")
                .append(" lgst.order_id, ")
                .append(" lgdt.logistics_id, ")
                .append(" lgdt.package_id ");
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
                .append(" stor.store_id        AS  storeId,")
                .append(" stor.name            AS  storeName,")
                .append(" ordr.order_date      AS  orderDate,")
                .append(" count(ordr.order_id) AS  totalOrder,")

                .append(" pstc.post            AS  postName,")
                .append(" pstc.course          AS  courseName,")
                .append(" SUM(prdc.price)      AS  amount,")
//  Logistic
                .append(" lgdt.amount          AS  logisticAmount,")
                .append(" lgdt.package_id      AS  logisticPackageId, ")
//                .append(" pckg.pakage_id       AS  packageId1, ")
//                .append(" pckg.name            AS  logisticPackageName,")
//                .append(" pckg.full_load_weight   AS logisticTotalWeight, ")
//                .append(" pckg.full_load_capacity AS logisticTotalCapacity, ")
//  Delivery
                .append(" ddt2.amount          AS  deliveryAmount,")
                .append(" ddt2.package_id      AS  deliveryPackageId ")
//                .append(" pkg2.pakage_id       AS  packageId2,")
//                .append(" pkg2.name            AS  deliveryPackageName, ")
//                .append(" pkg2.full_load_weight   AS deliveryWeight,")
//                .append(" pkg2.full_load_capacity AS deliveryCapacity ")
                ;
    }

    @Override
    public StringBuilder buildCount() {
        return new StringBuilder("SELECT COUNT(DISTINCT(ordt.order_detail_id)) ").append(buildFrom());
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
//  logisticView
                .append(" INNER JOIN t_logistics         AS lgst  ON lgst.order_id        =  ordr.order_id ")
                .append(" INNER JOIN t_logistics_detail  AS lgdt  ON lgdt.logistics_id    =  lgst.logistics_id ")
                .append(" INNER JOIN m_package           AS pckg  ON pckg.pakage_id       =  lgdt.package_id ")
//  deliveryView
                .append(" INNER JOIN t_delivery          AS dlvr  ON dlvr.order_id        = ordr.order_id ")
                .append(" INNER JOIN t_delivery_detail   AS ddt2  ON ddt2.delivery_id     = dlvr.delivery_id ")
                .append(" INNER JOIN m_package           AS pkg2  ON pkg2.pakage_id       = ddt2.package_id ");
    }

}
