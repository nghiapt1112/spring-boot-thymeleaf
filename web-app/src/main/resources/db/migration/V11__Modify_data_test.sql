UPDATE m_store SET `name` = '店舗 1' WHERE `store_id` = '507e201e890c08729ab860ca';
UPDATE m_store SET `name` = '店舗 2' WHERE `store_id` = '507e201e890c08729ab860cb';
UPDATE m_store SET `name` = '店舗 3' WHERE `store_id` = '507e201e890c08729ab860cc';

INSERT INTO m_store(`store_id`, `tenant_id`, `code`, `name`, `major_area`, `area`, `create_date`, `create_user`)
 VALUES
('507e201e890c08729ab860cg', 1, 'store_code7', '店舗 7', '大エリア 7', 'エリア 7', '2018-11-14 14:35:50', '')
;
