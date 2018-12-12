CREATE OR REPLACE VIEW `v_logicstic` AS
SELECT
    l.logistics_id AS logistics_id,
    o.order_date AS order_date,
    s.NAME AS store_name,
    pc.post AS post,
    od.amount AS amount,
    pr.price AS price,
    p.package_id AS package_id,
    p.NAME AS package_name,
    pc.course AS course,
    o.order_id AS order_id
FROM
    t_logistics l
    LEFT OUTER JOIN t_logistics_detail ld ON l.logistics_id = ld.logistics_id
    LEFT OUTER JOIN t_order o ON l.order_id = o.order_id
    LEFT OUTER JOIN t_order_detail od ON o.order_id = od.order_id
    LEFT OUTER JOIN m_product pr ON pr.product_id = od.product_id
    LEFT OUTER JOIN m_post_course pc ON pc.post_course_id = o.post_course_id
    LEFT OUTER JOIN m_store s ON pc.store_id = s.store_id
    LEFT OUTER JOIN m_package p ON p.package_id = ld.package_id
ORDER BY
    l.logistics_id;