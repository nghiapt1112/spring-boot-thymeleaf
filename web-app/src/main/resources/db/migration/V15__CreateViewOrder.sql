CREATE VIEW `v_order` AS
select	
    o.order_id,
    o.order_date as "日付",					
	s.name as "店舗",					
	pc.post as "便",					
	pr.name as "商品",					
	od.amount as "個数",					
	pr.category1 as "大分類",					
	pr.category2 as "中分類",					
	pr.category3 as "小分類"	
						
from	t_order o join t_order_detail od on o.order_id=od.order_id					
		join m_product pr on pr.product_id = od.product_id				
				
		join m_post_course pc on pc.post_course_id=o.post_course_id		
		join m_store s on s.store_id=pc.store_id