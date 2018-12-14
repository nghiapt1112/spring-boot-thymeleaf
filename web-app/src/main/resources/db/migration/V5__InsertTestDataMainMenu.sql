ALTER TABLE t_order MODIFY COLUMN store_id varchar(36);
ALTER TABLE t_logistics_detail MODIFY COLUMN logistics_detail_id varchar(36);

INSERT INTO `m_post_course`(`tenant_id`, `post_course_id`, `store_id`, `post`, `course`, `create_date`, `create_user`) VALUES (1,'999f191e810c19119de222aa','507e201e890c08729ab860ca','便１','コース１','2018-11-27 14:35:50','507f191e810c19729de860ea');
INSERT INTO `m_post_course`(`tenant_id`, `post_course_id`, `store_id`, `post`, `course`, `create_date`, `create_user`) VALUES (1,'999f191e810c19119de222ab','507e201e890c08729ab860ca','便2','コース2','2018-11-27 14:35:50','507f191e810c19729de860ea');
INSERT INTO `m_post_course`(`tenant_id`, `post_course_id`, `store_id`, `post`, `course`, `create_date`, `create_user`) VALUES (1,'999f191e810c19119de222ac','507e201e890c08729ab860cb','便１','コース3','2018-11-27 14:35:50','507f191e810c19729de860ea');

INSERT INTO `m_product`(`tenant_id`, `product_id`, `code`, `name`, `unit`, `price`, `category1`, `category2`, `category3`, `temperature`, `create_date`, `create_user`) VALUES (1,'507f191e810c19119de222aa','0001','おにぎり','個','150','食品','米類','カテゴリー１','冷蔵','2018-11-27 14:35:50','507f191e810c19729de860ea');
INSERT INTO `m_product`(`tenant_id`, `product_id`, `code`, `name`, `unit`, `price`, `category1`, `category2`, `category3`, `temperature`, `create_date`, `create_user`) VALUES (1,'507f191e810c19119de222ab','0002','サンドイッチ','個','250','食品','パン類','カテゴリー２','定温','2018-11-27 14:35:50','507f191e810c19729de860ea');
INSERT INTO `m_product`(`tenant_id`, `product_id`, `code`, `name`, `unit`, `price`, `category1`, `category2`, `category3`, `temperature`, `create_date`, `create_user`) VALUES (1,'507f191e810c19119de222ac','0003','マグロ','ぴき','500','魚介','海鮮','カテゴリー３','超低温','2018-11-27 14:35:50','507f191e810c19729de860ea');

INSERT INTO `m_package`(`tenant_id`, package_id, `name`, `unit`, `empty_weight`, `full_load_weight`, `empty_capacity`, `full_load_capacity`, `create_date`, `create_user`) VALUES(1,'507f191e666c19119de222bb','ばんじゅう','個','1','10','2','20','2018-11-27 14:35:50','507f191e810c19729de860ea');
INSERT INTO `m_package`(`tenant_id`, package_id, `name`, `unit`, `empty_weight`, `full_load_weight`, `empty_capacity`, `full_load_capacity`, `create_date`, `create_user`) VALUES(1,'507f191e666c19119de222bc','箱','個','2','15','3','15','2018-11-27 14:35:50','507f191e810c19729de860ea');
INSERT INTO `m_package`(`tenant_id`, package_id, `name`, `unit`, `empty_weight`, `full_load_weight`, `empty_capacity`, `full_load_capacity`, `create_date`, `create_user`) VALUES(1,'507f191e666c19119de222bd','段ボール','個','0.5','18','2.5','25','2018-11-27 14:35:50','507f191e810c19729de860ea');
INSERT INTO `m_package`(`tenant_id`, package_id, `name`, `unit`, `empty_weight`, `full_load_weight`, `empty_capacity`, `full_load_capacity`, `create_date`, `create_user`) VALUES(1,'507f191e666c19119de222be','ケース','個','3','28.5','3','19','2018-11-27 14:35:50','507f191e810c19729de860ea');

INSERT INTO `t_order`(`tenant_id`, `order_id`, `order_date`, `store_id`, `post`, `create_date`, `create_user`)  VALUES (1,'507f191e810c19119de860ea','2018-11-27','507e201e890c08729ab860ca','便１','2018-11-27 14:35:50','507f191e810c19729de860ea');
INSERT INTO `t_order`(`tenant_id`, `order_id`, `order_date`, `store_id`, `post`, `create_date`, `create_user`)  VALUES (1,'507f191e810c19110de860ja','2018-11-27','507e201e890c08729ab860ca','便２','2018-11-27 14:35:50','507f191e810c19729de860ea');
INSERT INTO `t_order`(`tenant_id`, `order_id`, `order_date`, `store_id`, `post`, `create_date`, `create_user`)  VALUES (1,'507f191e810c22110de860ja','2018-11-27','507e201e890c08729ab860cb','便１','2018-11-27 14:35:50','507f191e810c19729de860ea');
INSERT INTO `t_order`(`tenant_id`, `order_id`, `order_date`, `store_id`, `post`, `create_date`, `create_user`)  VALUES (1,'667f191e810c22110de860ja','2018-11-27','507e201e890c08729ab860cb','便２','2018-11-27 14:35:50','507f191e810c19729de860ea');
INSERT INTO `t_order`(`tenant_id`, `order_id`, `order_date`, `store_id`, `post`, `create_date`, `create_user`)  VALUES (1,'667f191e810c22110de86aaa','2018-11-27','507e201e890c08729ab860cc','便１','2018-11-27 14:35:50','507f191e810c19729de860ea');

INSERT INTO `t_order_detail`(`tenant_id`, `order_detail_id`, `order_id`, `product_id`, `amount`, `create_date`, `create_user`) VALUES(1,'507f191e810c19119de86eee','507f191e810c19119de860ea','507f191e810c19119de222aa',100,'2018-11-27 14:35:50','507f191e810c19729de860ea');
INSERT INTO `t_order_detail`(`tenant_id`, `order_detail_id`, `order_id`, `product_id`, `amount`, `create_date`, `create_user`) VALUES(1,'507f191e810c19119de86eef','507f191e810c19110de860ja','507f191e810c19119de222ab',150,'2018-11-27 14:35:50','507f191e810c19729de860ea');
INSERT INTO `t_order_detail`(`tenant_id`, `order_detail_id`, `order_id`, `product_id`, `amount`, `create_date`, `create_user`) VALUES(1,'507f191e810c19119de86eeｇ','507f191e810c22110de860ja','507f191e810c19119de222ac',200,'2018-11-27 14:35:50','507f191e810c19729de860ea');
INSERT INTO `t_order_detail`(`tenant_id`, `order_detail_id`, `order_id`, `product_id`, `amount`, `create_date`, `create_user`) VALUES(1,'507f191e810c19119de86eeｈ','667f191e810c22110de86aaa','507f191e810c19119de222ac',500,'2018-11-27 14:35:50','507f191e810c19729de860ea');

INSERT INTO `t_logistics`(`tenant_id`, `logistics_id`, `order_id`, `create_date`, `create_user`) VALUES (1,'667f191e810c19119de860ea','507f191e810c19119de860ea','2018-11-27 14:35:50','507f191e810c19729de860ea');
INSERT INTO `t_logistics`(`tenant_id`, `logistics_id`, `order_id`, `create_date`, `create_user`) VALUES (1,'667f191e810c19119de880ee','507f191e810c19119de860ea','2018-11-27 14:35:50','507f191e810c19729de860ea');
INSERT INTO `t_logistics`(`tenant_id`, `logistics_id`, `order_id`, `create_date`, `create_user`) VALUES (1,'667f191e810c19119de880ef','507f191e810c19119de860ea','2018-11-27 14:35:50','507f191e810c19729de860ea');
INSERT INTO `t_logistics`(`tenant_id`, `logistics_id`, `order_id`, `create_date`, `create_user`) VALUES (1,'667f191e810c19119de880eg','507f191e810c19110de860ja','2018-11-27 14:35:50','507f191e810c19729de860ea');

INSERT INTO `t_logistics_detail`(`tenant_id`, `logistics_detail_id`, `logistics_id`, `package_id`, `amount`, `create_date`, `create_user`) VALUES (1,'887f191e810c19119de860ea','667f191e810c19119de860ea','507f191e666c19119de222bb','3','2018-11-27 14:35:50','507f191e810c19729de860ea');
INSERT INTO `t_logistics_detail`(`tenant_id`, `logistics_detail_id`, `logistics_id`, `package_id`, `amount`, `create_date`, `create_user`) VALUES (1,'887f191e810c19119de860eb','667f191e810c19119de880ee','507f191e666c19119de222bc','5','2018-11-27 14:35:50','507f191e810c19729de860ea');
INSERT INTO `t_logistics_detail`(`tenant_id`, `logistics_detail_id`, `logistics_id`, `package_id`, `amount`, `create_date`, `create_user`) VALUES (1,'887f191e810c19119de860ec','667f191e810c19119de880ef','507f191e666c19119de222bd','6','2018-11-27 14:35:50','507f191e810c19729de860ea');
INSERT INTO `t_logistics_detail`(`tenant_id`, `logistics_detail_id`, `logistics_id`, `package_id`, `amount`, `create_date`, `create_user`) VALUES (1,'887f191e810c19119de860ed','667f191e810c19119de880eg','507f191e666c19119de222be','7','2018-11-27 14:35:50','507f191e810c19729de860ea');

