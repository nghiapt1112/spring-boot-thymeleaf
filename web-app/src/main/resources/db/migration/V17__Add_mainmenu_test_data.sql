SET FOREIGN_KEY_CHECKS = 0;

INSERT INTO `m_product`(`tenant_id`, `product_id`, `code`, `name`, `unit`, `price`, `category1`, `category2`, `category3`)
VALUES
(2, 'product_id_1', '0001', 'name1', 'unit', 10.0, 'cat1', 'cat2', 'cat3'),
(2, 'product_id_2', '0002', 'name2', 'unit', 10.0, 'cat1', 'cat2', 'cat3'),
(2, 'product_id_3', '0003', 'name3', 'unit', 10.0, 'cat1', 'cat2', 'cat3'),
(2, 'product_id_4', '0004', 'name4', 'unit', 10.0, 'cat1', 'cat2', 'cat3'),
(2, 'product_id_5', '0005', 'name5', 'unit', 10.0, 'cat1', 'cat2', 'cat3'),
(2, 'product_id_6', '0006', 'name6', 'unit', 10.0, 'cat1', 'cat2', 'cat3'),
(2, 'product_id_7', '0007', 'name7', 'unit', 10.0, 'cat1', 'cat2', 'cat3'),
(2, 'product_id_8', '0008', 'name8', 'unit', 10.0, 'cat1', 'cat2', 'cat3'),
(2, 'product_id_9', '0009', 'name9', 'unit', 10.0, 'cat1', 'cat2', 'cat3');


INSERT INTO `t_order_detail`(`tenant_id`, `order_detail_id`, `order_id`, `product_id`, `amount`)
VALUES
(2, 'order_detail_id_1', 'order_id_1', 'product_id_1', 100),
(2, 'order_detail_id_2', 'order_id_1', 'product_id_2', 100),
(2, 'order_detail_id_3', 'order_id_1', 'product_id_3', 100),
(2, 'order_detail_id_4', 'order_id_2', 'product_id_4', 100),
(2, 'order_detail_id_5', 'order_id_2', 'product_id_5', 100),
(2, 'order_detail_id_6', 'order_id_2', 'product_id_6', 100),
(2, 'order_detail_id_7', 'order_id_2', 'product_id_7', 100),
(2, 'order_detail_id_8', 'order_id_3', 'product_id_8', 100),
(2, 'order_detail_id_9', 'order_id_4', 'product_id_9', 100),
(2, 'order_detail_id_10','order_id_4','product_id_10', 100)
;


INSERT INTO `t_order`(`tenant_id`, `order_id`, `order_date`, `post_course_id`,`create_date`, `create_user`)
VALUES
(2,'order_id_1','2018-11-27','post_course_id_1','2018-12-11 00:00:50','507f191e810c19729de860ea'),
(2,'order_id_2','2018-11-27','post_course_id_2','2018-12-11 00:00:50','507f191e810c19729de860ea'),
(2,'order_id_3','2018-11-27','post_course_id_3','2018-12-11 00:00:50','507f191e810c19729de860ea'),
(2,'order_id_4','2018-11-28','post_course_id_4','2018-12-11 00:00:50','507f191e810c19729de860ea'),
(2,'order_id_5','2018-11-28','post_course_id_5','2018-12-11 00:00:50','507f191e810c19729de860ea'),
(2,'order_id_6','2018-11-28','post_course_id_6','2018-12-11 00:00:50','507f191e810c19729de860ea'),
(2,'order_id_7','2018-11-29','post_course_id_7','2018-12-11 00:00:50','507f191e810c19729de860ea'),
(2,'order_id_8','2018-11-29','post_course_id_8','2018-12-11 00:00:50','507f191e810c19729de860ea')
;


INSERT INTO `t_logistics`(`tenant_id`, `logistics_id`, `order_id`, `create_date`, `create_user`)
 VALUES
(2,'logs_id_1', 'order_id_1','2018-12-11 00:00:50','507f191e810c19729de860ea'),
(2,'logs_id_2', 'order_id_1','2018-12-11 00:00:50','507f191e810c19729de860ea'),
(2,'logs_id_3', 'order_id_1','2018-12-11 00:00:50','507f191e810c19729de860ea'),
(2,'logs_id_4', 'order_id_2','2018-12-11 00:00:50','507f191e810c19729de860ea'),
(2,'logs_id_5', 'order_id_2','2018-12-11 00:00:50','507f191e810c19729de860ea'),
(2,'logs_id_6', 'order_id_2','2018-12-11 00:00:50','507f191e810c19729de860ea'),
(2,'logs_id_7', 'order_id_3','2018-12-11 00:00:50','507f191e810c19729de860ea'),
(2,'logs_id_8', 'order_id_3','2018-12-11 00:00:50','507f191e810c19729de860ea'),
(2,'logs_id_9', 'order_id_3','2018-12-11 00:00:50','507f191e810c19729de860ea'),
(2,'logs_id_10','order_id_4','2018-12-11 00:00:50','507f191e810c19729de860ea'),
(2,'logs_id_11','order_id_4','2018-12-11 00:00:50','507f191e810c19729de860ea'),
(2,'logs_id_12','order_id_4','2018-12-11 00:00:50','507f191e810c19729de860ea'),
(2,'logs_id_13','order_id_5','2018-12-11 00:00:50','507f191e810c19729de860ea'),
(2,'logs_id_14','order_id_5','2018-12-11 00:00:50','507f191e810c19729de860ea')
;
INSERT INTO `m_package`(`tenant_id`, `package_id`, `name`, `unit`, `empty_weight`, `full_load_weight`, `empty_capacity`, `full_load_capacity`)
VALUES
(2, 'pkg_id_1', 'name1', 'unit1', 1, 100, 1, 200),
(2, 'pkg_id_2', 'name2', 'unit2', 1, 100, 1, 200),
(2, 'pkg_id_3', 'name3', 'unit3', 1, 100, 1, 200),
(2, 'pkg_id_4', 'name4', 'unit4', 1, 100, 1, 200),
(2, 'pkg_id_5', 'name5', 'unit5', 1, 100, 1, 200),
(2, 'pkg_id_6', 'name6', 'unit6', 1, 100, 1, 200),
(2, 'pkg_id_7', 'name7', 'unit7', 1, 100, 1, 200)
;


INsert into t_logistics_detail(tenant_id, logistics_detail_id, logistics_id, package_id, amount, create_date, create_user)
VALUES
(2,'log_detail_id_1','logs_id_1','pkg_id_1','10','2018-11-27 14:35:50','507f191e810c19729de860ea'),
(2,'log_detail_id_2','logs_id_1','pkg_id_1','10','2018-11-27 14:35:50','507f191e810c19729de860ea'),
(2,'log_detail_id_3','logs_id_1','pkg_id_1','10','2018-11-27 14:35:50','507f191e810c19729de860ea'),
(2,'log_detail_id_4','logs_id_1','pkg_id_1','10','2018-11-27 14:35:50','507f191e810c19729de860ea'),
(2,'log_detail_id_5','logs_id_1','pkg_id_1','10','2018-11-27 14:35:50','507f191e810c19729de860ea'),
(2,'log_detail_id_6','logs_id_1','pkg_id_1','10','2018-11-27 14:35:50','507f191e810c19729de860ea'),
(2,'log_detail_id_7','logs_id_1','pkg_id_1','10','2018-11-27 14:35:50','507f191e810c19729de860ea'),
(2,'log_detail_id_8','logs_id_1','pkg_id_1','10','2018-11-27 14:35:50','507f191e810c19729de860ea'),
(2,'log_detail_id_9','logs_id_1','pkg_id_1','10','2018-11-27 14:35:50','507f191e810c19729de860ea'),
(2,'log_detail_id_10','logs_id_1','pkg_id_1','10','2018-11-27 14:35:50','507f191e810c19729de860ea'),
(2,'log_detail_id_11','logs_id_1','pkg_id_1','10','2018-11-27 14:35:50','507f191e810c19729de860ea'),
(2,'log_detail_id_12','logs_id_1','pkg_id_1','10','2018-11-27 14:35:50','507f191e810c19729de860ea'),
(2,'log_detail_id_13','logs_id_1','pkg_id_1','10','2018-11-27 14:35:50','507f191e810c19729de860ea'),
(2,'log_detail_id_14','logs_id_1','pkg_id_1','10','2018-11-27 14:35:50','507f191e810c19729de860ea'),
(2,'log_detail_id_15','logs_id_1','pkg_id_1','10','2018-11-27 14:35:50','507f191e810c19729de860ea'),
(2,'log_detail_id_16','logs_id_1','pkg_id_1','10','2018-11-27 14:35:50','507f191e810c19729de860ea'),
(2,'log_detail_id_17','logs_id_1','pkg_id_1','10','2018-11-27 14:35:50','507f191e810c19729de860ea'),
(2,'log_detail_id_18','logs_id_1','pkg_id_1','10','2018-11-27 14:35:50','507f191e810c19729de860ea'),
(2,'log_detail_id_19','logs_id_1','pkg_id_1','10','2018-11-27 14:35:50','507f191e810c19729de860ea'),
(2,'log_detail_id_20','logs_id_1','pkg_id_1','10','2018-11-27 14:35:50','507f191e810c19729de860ea'),
(2,'log_detail_id_21','logs_id_1','pkg_id_1','10','2018-11-27 14:35:50','507f191e810c19729de860ea'),
(2,'log_detail_id_22','logs_id_1','pkg_id_1','10','2018-11-27 14:35:50','507f191e810c19729de860ea'),
(2,'log_detail_id_23','logs_id_1','pkg_id_1','10','2018-11-27 14:35:50','507f191e810c19729de860ea'),
(2,'log_detail_id_24','logs_id_1','pkg_id_1','10','2018-11-27 14:35:50','507f191e810c19729de860ea'),
(2,'log_detail_id_25','logs_id_1','pkg_id_1','10','2018-11-27 14:35:50','507f191e810c19729de860ea'),
(2,'log_detail_id_26','logs_id_1','pkg_id_1','10','2018-11-27 14:35:50','507f191e810c19729de860ea'),
(2,'log_detail_id_27','logs_id_1','pkg_id_1','10','2018-11-27 14:35:50','507f191e810c19729de860ea'),
(2,'log_detail_id_28','logs_id_1','pkg_id_1','10','2018-11-27 14:35:50','507f191e810c19729de860ea'),
(2,'log_detail_id_29','logs_id_1','pkg_id_1','10','2018-11-27 14:35:50','507f191e810c19729de860ea'),
(2,'log_detail_id_30','logs_id_1','pkg_id_1','10','2018-11-27 14:35:50','507f191e810c19729de860ea'),
(2,'log_detail_id_31','logs_id_1','pkg_id_1','10','2018-11-27 14:35:50','507f191e810c19729de860ea'),
(2,'log_detail_id_32','logs_id_1','pkg_id_1','10','2018-11-27 14:35:50','507f191e810c19729de860ea'),
(2,'log_detail_id_33','logs_id_1','pkg_id_1','10','2018-11-27 14:35:50','507f191e810c19729de860ea')
;
SET FOREIGN_KEY_CHECKS = 1;