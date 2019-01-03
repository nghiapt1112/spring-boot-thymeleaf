--
--
-- Modify order view
--
--
CREATE OR REPLACE VIEW `v_delivery` AS
SELECT l.tenant_id AS tenant_id,
       o.order_id AS order_id,
       ld.delivery_detail_id AS delivery_detail_id,
       o.order_date AS order_date,
       s.NAME AS store_name,
       pc.post AS post,

  (SELECT sum(amount)
   FROM t_order_detail tod
   WHERE tod.order_id = o.order_id) AS amount,

  (SELECT sum(price)
   FROM m_product mp
   JOIN t_order_detail tod ON mp.product_id = tod.product_id
   JOIN t_order to1 ON tod.order_id = to1.order_id
   WHERE to1.order_id = o.order_id) AS price,
       p.package_id AS package_id,
       p.NAME AS package_name,
       ld.amount AS package_amount,
       p.full_load_weight*ld.amount AS full_load_weight,
       p.full_load_capacity*ld.amount AS full_load_capacity,
       pc.course AS course
FROM t_delivery l
LEFT OUTER JOIN t_delivery_detail ld ON l.delivery_id = ld.delivery_id
LEFT OUTER JOIN t_order o ON l.order_id = o.order_id
LEFT OUTER JOIN m_post_course pc ON pc.post_course_id = o.post_course_id
LEFT OUTER JOIN m_store s ON pc.store_id = s.store_id
LEFT OUTER JOIN m_package p ON p.package_id = ld.package_id
ORDER BY o.order_date;

--
--
-- Modify logistic view
--
--
CREATE OR REPLACE VIEW `v_logicstic` AS
SELECT
    l.tenant_id AS tenant_id,
    o.order_id AS order_id,
    ld.logistics_detail_id AS logistics_detail_id,
    o.order_date AS order_date,
    s.NAME AS store_name,
    pc.post AS post,
    
    (SELECT sum(amount)
   FROM t_order_detail tod
   WHERE tod.order_id = o.order_id) AS amount,
   
    (SELECT sum(price)
   FROM m_product mp
   JOIN t_order_detail tod ON mp.product_id = tod.product_id
   JOIN t_order to1 ON tod.order_id = to1.order_id
   WHERE to1.order_id = o.order_id) AS price,
   
    p.package_id AS package_id,
    p.NAME AS package_name,
    ld.amount AS package_amount,
    p.full_load_weight*ld.amount AS full_load_weight,
    p.full_load_capacity*ld.amount AS full_load_capacity,
    pc.course AS course
FROM
    t_logistics l
    LEFT OUTER JOIN t_logistics_detail ld ON l.logistics_id = ld.logistics_id
    LEFT OUTER JOIN t_order o ON l.order_id = o.order_id
    LEFT OUTER JOIN m_post_course pc ON pc.post_course_id = o.post_course_id
    LEFT OUTER JOIN m_store s ON pc.store_id = s.store_id
    LEFT OUTER JOIN m_package p ON p.package_id = ld.package_id
ORDER BY
    o.order_date;