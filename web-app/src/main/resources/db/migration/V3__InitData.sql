INSERT INTO m_tenant (`tenant_id`, `name`, `user_limit_number`, `create_date`, `create_user`) 
VALUES
(1, 'tenant1.com', 1000, '2018-11-13 15:51:06', '1'),
(2, 'tenant2.com', 1000, '2018-11-13 15:51:06', '1'),
(3, 'tenant3.com', 1000, '2018-11-13 15:51:06', '1')
;

INSERT INTO m_user (`tenant_id`, `user_id`, `email`, `password`, `name`, `role`, `create_date`)
VALUES
(1, '507f191e810c19729de860ea', 'admin@tenant1.com', '$2a$10$wW33WsvWzFMAJw7jICe2re24P5vcUeAjwYIO2lF8KGEj2B0gcMw06', '鈴木　勇', 1, '2018-11-13 16:07:42'),
(1, '507f191e810c19729de860eb', 'user@tenant1.com', '$2a$10$wW33WsvWzFMAJw7jICe2re24P5vcUeAjwYIO2lF8KGEj2B0gcMw06', '小林　茂', 0, '2018-11-13 16:07:42'),
(1, '507f191e810c19729de860ec', 'view@tenant1.com', '$2a$10$wW33WsvWzFMAJw7jICe2re24P5vcUeAjwYIO2lF8KGEj2B0gcMw06', '伊藤　博', 0, '2018-11-13 16:07:42'),
(1, '507f191e810c19729de860ed', 'free@tenant1.com', '$2a$10$wW33WsvWzFMAJw7jICe2re24P5vcUeAjwYIO2lF8KGEj2B0gcMw06', '田中　博', 0, '2018-11-13 16:07:42')
;

INSERT INTO m_store(`store_id`, `tenant_id`, `code`, `name`, `major_area`, `area`, `create_date`, `create_user`)
 VALUES
('507e201e890c08729ab860ca', 1, 'store_code1', '店舗１', '大エリア１', 'エリア１', '2018-11-14 14:35:50', ''),
('507e201e890c08729ab860cb', 1, 'store_code2', '店舗２', '大エリア２', 'エリア２', '2018-11-14 14:35:50', ''),
('507e201e890c08729ab860cc', 1, 'store_code3', '店舗３', '大エリア３', 'エリア３', '2018-11-14 14:35:50', '')
;

INSERT INTO m_user_store_authority(`tenant_id`, `user_store_authority_id`, `store_id`, `user_id`, `authority`, `create_date`, `create_user`)
VALUES
(1, '507f1f77bcf86cd799439011', '507e201e890c08729ab860ca', '507f191e810c19729de860ea', 1, '2018-11-13 16:18:08', '-1'),
(1, '507f1f77bcf86cd799439012', '507e201e890c08729ab860ca', '507f191e810c19729de860eb', 1, '2018-11-13 16:18:08', '-1'),
(1, '507f1f77bcf86cd799439013', '507e201e890c08729ab860ca', '507f191e810c19729de860ec', 1, '2018-11-13 16:18:08', '-1')
;