CREATE OR REPLACE VIEW `v_order` AS
select
  o.order_id,
  od.order_detail_id,
  o.tenant_id,
  o.order_date as "order_date",
  s.name as "store_name",
  pc.post as "post_name",
  pr.name as "product_name",
  od.amount as "order_amount",
  pr.category1 as "first_category",
  pr.category2 as "second_category",
  pr.category3 as "third_category"

from  t_order o
  join t_order_detail od on o.order_id=od.order_id
  join m_product pr on pr.product_id = od.product_id
  join m_post_course pc on pc.post_course_id=o.post_course_id
  join m_store s on s.store_id=pc.store_id

order by
  o.order_date