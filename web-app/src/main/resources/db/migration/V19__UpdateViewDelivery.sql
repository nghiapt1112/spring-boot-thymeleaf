CREATE OR REPLACE VIEW `v_delivery` AS
SELECT
    l.tenant_id AS tenant_id,
    l.delivery_id AS delivery_id,
    o.order_date AS order_date,
    s.NAME AS store_name,
    pc.post AS post,
    od.amount AS amount,
    pr.price AS price,
    p.package_id AS package_id,
    p.NAME AS package_name,
    p.full_load_weight AS full_load_weight,
    p.full_load_capacity AS full_load_capacity,
    pc.course AS course,
    o.order_id AS order_id
FROM
    t_delivery l
    LEFT OUTER JOIN t_delivery_detail ld ON l.delivery_id = ld.delivery_id
    LEFT OUTER JOIN t_order o ON l.order_id = o.order_id
    LEFT OUTER JOIN t_order_detail od ON o.order_id = od.order_id
    LEFT OUTER JOIN m_product pr ON pr.product_id = od.product_id
    LEFT OUTER JOIN m_post_course pc ON pc.post_course_id = o.post_course_id
    LEFT OUTER JOIN m_store s ON pc.store_id = s.store_id
    LEFT OUTER JOIN m_package p ON p.package_id = ld.package_id
ORDER BY
    o.order_date,
    l.delivery_id;