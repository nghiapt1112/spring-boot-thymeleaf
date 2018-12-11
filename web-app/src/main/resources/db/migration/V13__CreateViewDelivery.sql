CREATE VIEW `v_delivery` AS
SELECT
    t0.order_id AS order_id,
    t0.order_date AS 日付,
    t0.store_name AS 店舗,
    t0.post AS 便,
    t0.amount AS 発注個数,
    t0.price AS 金額,
    t1.packageId AS ばんじゅう,
    t2.cases AS ケース,
    t3.BOX AS 箱,
    ((COALESCE(t1.weight, 0) + COALESCE(t2.weight, 0)) + COALESCE(t3.weight, 0)) AS 総重量,
    ((COALESCE(t1.cap, 0) + COALESCE(t2.cap, 0)) + COALESCE(t3.cap, 0)) AS 総才数,
    t0.course AS コース
FROM
    (SELECT
        l.delivery_id AS delivery_id,
            o.order_date AS order_date,
            s.name AS store_name,
            pc.post AS post,
            SUM(od.amount) AS amount,
            SUM(pr.price) AS price,
            pc.course AS course,
            o.order_id AS order_id
    FROM
        t_delivery l
    JOIN t_delivery_detail ld ON l.delivery_id = ld.delivery_id
    JOIN t_order o ON l.order_id = o.order_id
    JOIN t_order_detail od ON o.order_id = od.order_id
    JOIN m_product pr ON pr.product_id = od.product_id
    JOIN m_post_course pc ON pc.post_course_id = o.post_course_id
    JOIN m_store s ON pc.store_id = s.store_id
    GROUP BY o.order_id) t0
        LEFT JOIN
    (SELECT
        l.delivery_id AS delivery_id,
            SUM(od.amount) AS amount,
            SUM(pr.price) AS price,
            COALESCE(COUNT(p.package_id), 0) AS packageId,
            SUM(p.full_load_weight) AS weight,
            SUM(p.full_load_capacity) AS cap,
            o.order_id AS order_id,
            pc.course AS course
    FROM
        t_delivery l
    JOIN t_delivery_detail ld ON l.delivery_id = ld.delivery_id
    JOIN m_package p ON ld.package_id = p.package_id
    JOIN t_order o ON l.order_id = o.order_id
    JOIN t_order_detail od ON o.order_id = od.order_id
    JOIN m_product pr ON pr.product_id = od.product_id
    JOIN m_post_course pc ON pc.post_course_id = o.post_course_id
    JOIN m_store s ON pc.store_id = s.store_id
    WHERE
        p.name = 'ばんじゅう'
    GROUP BY o.order_id) t1 ON t0.delivery_id = t1.delivery_id
        LEFT JOIN
    (SELECT
        l.delivery_id AS delivery_id,
            SUM(od.amount) AS amount,
            SUM(pr.price) AS price,
            COALESCE(COUNT(p.package_id), 0) AS cases,
            COALESCE(SUM(p.full_load_weight), 0) AS weight,
            SUM(p.full_load_capacity) AS cap,
            o.order_id AS order_id,
            pc.course AS course
    FROM
        t_delivery l
    JOIN t_delivery_detail ld ON l.delivery_id = ld.delivery_id
    JOIN m_package p ON ld.package_id = p.package_id
    JOIN t_order o ON l.order_id = o.order_id
    JOIN t_order_detail od ON o.order_id = od.order_id
    JOIN m_product pr ON pr.product_id = od.product_id
    JOIN m_post_course pc ON pc.post_course_id = o.post_course_id
    JOIN m_store s ON pc.store_id = s.store_id
    WHERE
        p.name = 'ケース'
    GROUP BY o.order_id) t2 ON t0.delivery_id = t2.delivery_id
        LEFT JOIN
    (SELECT
        l.delivery_id AS delivery_id,
            SUM(od.amount) AS amount,
            SUM(pr.price) AS price,
            COUNT(p.package_id) AS BOX,
            COALESCE(SUM(p.full_load_weight), 0) AS weight,
            SUM(p.full_load_capacity) AS cap,
            o.order_id AS order_id,
            pc.course AS course
    FROM
        t_delivery l
    JOIN t_delivery_detail ld ON l.delivery_id = ld.delivery_id
    JOIN m_package p ON ld.package_id = p.package_id
    JOIN t_order o ON l.order_id = o.order_id
    JOIN t_order_detail od ON o.order_id = od.order_id
    JOIN m_product pr ON pr.product_id = od.product_id
    JOIN m_post_course pc ON pc.post_course_id = o.post_course_id
    JOIN m_store s ON pc.store_id = s.store_id
    WHERE
        p.name = '箱'
    GROUP BY o.order_id) t3 ON t0.delivery_id = t3.delivery_id