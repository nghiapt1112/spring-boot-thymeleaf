--
--
-- Modify logistic view
--
--
CREATE OR REPLACE VIEW `v_logicstic` AS
SELECT
	l.tenant_id AS tenant_id,
	o.order_id AS order_id,
	IFNULL(ld.logistics_detail_id,'') AS logistics_detail_id,
	o.order_date AS order_date,
	s.name AS store_name,
	pc.post AS post,
	( SELECT sum( tod.amount ) FROM t_order_detail tod WHERE ( tod.order_id = o.order_id ) ) AS amount,
	(
	SELECT
		sum( mp.price )
	FROM
		(
			( m_product mp JOIN t_order_detail tod ON ( ( mp.product_id = tod.product_id ) ) )
			JOIN t_order to1 ON ( ( tod.order_id = to1.order_id ) )
		)
	WHERE
		( to1.order_id = o.order_id )
	) AS price,
	IFNULL(p.package_id,'') AS package_id,
	p.name AS package_name,
	IFNULL(ld.amount,0) AS package_amount,
	IFNULL(( p.full_load_weight * ld.amount ),0) AS full_load_weight,
	IFNULL(( p.full_load_capacity * ld.amount ),0) AS full_load_capacity,
	pc.course AS course
FROM
	(
		(
			(
				(
					( t_logistics l LEFT JOIN t_logistics_detail ld ON ( ( l.logistics_id = ld.logistics_id ) ) )
					LEFT JOIN t_order o ON ( ( l.order_id = o.order_id ) )
				)
				LEFT JOIN m_post_course pc ON ( ( pc.post_course_id = o.post_course_id ) )
			)
			LEFT JOIN m_store s ON ( ( pc.store_id = s.store_id ) )
		)
		LEFT JOIN m_package p ON ( ( p.package_id = ld.package_id ) )
	)
ORDER BY
	o.order_date