CREATE TABLE hibernate_sequence(
next_val BIGINT(20)
);
INSERT INTO hibernate_sequence (next_val) VALUE (1);

----------------------------------------------
----------------------------------------------
----------------------------------------------
----------------------------------------------

SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for m_tenant
-- ----------------------------
DROP TABLE IF EXISTS `m_tenant`;
CREATE TABLE `m_tenant`  (
  `tenant_id` int(11) NOT NULL COMMENT 'ID',
  `name` varchar(255) COMMENT '名称\r\n',
  `user_limit_number` int(5) NOT NULL,
  `create_date` timestamp(0) NULL COMMENT '作成日時',
  `create_user` varchar(36)  NULL COMMENT '作成ユーザーID',
  `update_date` timestamp(0) NULL COMMENT '更新日時',
  `update_user` varchar(36)  NULL COMMENT '更新ユーザーID',
  PRIMARY KEY (`tenant_id`) USING BTREE
) ;

-- ----------------------------
-- Table structure for m_package
-- ----------------------------
DROP TABLE IF EXISTS `m_package`;
CREATE TABLE `m_package`  (
  `tenant_id` int(11) NOT NULL COMMENT 'テナントID',
  `pakage_id` varchar(36) COMMENT 'ID',
  `name` varchar(255) COMMENT '荷姿名称',
  `unit` varchar(255)  NULL COMMENT '単位',
  `empty_weight` decimal(11, 2) NULL COMMENT '空の時の重量（ｋｇ）',
  `full_load_weight` decimal(11, 2) NULL COMMENT '満載の時の重量(kg)',
  `empty_capacity` decimal(11, 2) NULL COMMENT '空の時の容積 (才)',
  `full_load_capacity` decimal(11, 2) NULL COMMENT '満載の時の容積 (才)',
  `create_date` timestamp(0) NULL COMMENT '作成日時',
  `create_user` varchar(36)  NULL COMMENT '作成ユーザーID',
  `update_date` timestamp(0) NULL COMMENT '更新日時',
  `update_user` varchar(36)  NULL COMMENT '更新ユーザーID',
  PRIMARY KEY (`pakage_id`) USING BTREE,
  INDEX `tenant_id`(`tenant_id`) USING BTREE,
  CONSTRAINT `m_package_ibfk_1` FOREIGN KEY (`tenant_id`) REFERENCES `m_tenant` (`tenant_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ;

-- ----------------------------
-- Table structure for m_post_course
-- ----------------------------
DROP TABLE IF EXISTS `m_post_course`;
CREATE TABLE `m_post_course`  (
  `tenant_id` int(5) NOT NULL,
  `post_course_id` varchar(36)  ,
  `store_id` varchar(36)  NULL,
  `post` varchar(255)  NULL,
  `course` varchar(255)  NULL,
  `create_date` timestamp(0) NULL COMMENT '作成日時',
  `create_user` varchar(36) COMMENT '作成ユーザーID',
  `update_date` timestamp(0) NULL COMMENT '更新日時',
  `update_user` varchar(36)  NULL COMMENT '更新ユーザーID',
  PRIMARY KEY (`post_course_id`, `create_user`) USING BTREE,
  INDEX `tenant_id`(`tenant_id`) USING BTREE,
  INDEX `store_id`(`store_id`) USING BTREE,
  CONSTRAINT `m_post_course_ibfk_1` FOREIGN KEY (`tenant_id`) REFERENCES `m_tenant` (`tenant_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `m_post_course_ibfk_2` FOREIGN KEY (`store_id`) REFERENCES `m_stores` (`storeid`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ;

-- ----------------------------
-- Table structure for m_product
-- ----------------------------
DROP TABLE IF EXISTS `m_product`;
CREATE TABLE `m_product`  (
  `tenant_id` int(11) NOT NULL COMMENT 'テナントID',
  `product_id` varchar(36) COMMENT 'ID',
  `code` varchar(50)  NULL COMMENT 'コード',
  `name` varchar(255)  NULL COMMENT '商品名',
  `unit` varchar(50)  NULL COMMENT '単位',
  `price` decimal(10, 2) NULL,
  `category1` varchar(255)  NULL COMMENT '大分類',
  `category2` varchar(255)  NULL COMMENT '大分類',
  `category3` varchar(255)  NULL COMMENT '大分類',
  `temperature` varchar(255)  NULL COMMENT '温度',
  `create_date` timestamp(0) NULL COMMENT '作成日時',
  `create_user` varchar(36)  NULL COMMENT '作成ユーザーID',
  `update_date` timestamp(0) NULL COMMENT '更新日時',
  `update_user` varchar(36)  NULL COMMENT '更新ユーザーID',
  PRIMARY KEY (`product_id`) USING BTREE,
  INDEX `tenant_id`(`tenant_id`) USING BTREE,
  INDEX `product_id`(`product_id`) USING BTREE,  
  CONSTRAINT `m_product_ibfk_1` FOREIGN KEY (`tenant_id`) REFERENCES `m_tenant` (`tenant_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ;


-- ----------------------------
-- Table structure for m_stores
-- ----------------------------
DROP TABLE IF EXISTS `m_stores`;
CREATE TABLE `m_stores`  (
  `storeId` varchar(36) COMMENT 'ID',
  `tenant_id` int(11) NOT NULL COMMENT 'テナントID',
  `code` varchar(50)  NULL COMMENT 'コード',
  `name` varchar(255)  NULL COMMENT '名称',
  `major_area` varchar(255)  NULL COMMENT '大アレア',
  `area` varchar(255)  NULL,
  `post_course_id` varchar(36)  NULL,
  `create_date` timestamp(0) NULL COMMENT '作成日時',
  `create_user` varchar(36)  NULL COMMENT '作成ユーザーID',
  `update_date` timestamp(0) NULL COMMENT '更新日時',
  `update_user` varchar(36)  NULL COMMENT '更新ユーザーID',
  PRIMARY KEY (`storeId`) USING BTREE,
  INDEX `tenant_id`(`tenant_id`) USING BTREE,
  INDEX `storeId`(`storeId`) USING BTREE, 
  CONSTRAINT `m_stores_ibfk_1` FOREIGN KEY (`tenant_id`) REFERENCES `m_tenant` (`tenant_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ;



-- ----------------------------
-- Table structure for m_user
-- ----------------------------
DROP TABLE IF EXISTS `m_user`;
CREATE TABLE `m_user`  (
  `tenant_id` int(11) NOT NULL COMMENT 'テナントID\r\n',
  `user_id` varchar(36) COMMENT 'ID',
  `email` varchar(255) COMMENT 'メール',
  `password` varchar(100) COMMENT 'パスワード',
  `name` varchar(255)  NULL COMMENT '名前',
  `role` smallint(2) NULL COMMENT '権限',
  `create_date` timestamp(0) NULL COMMENT '作成日時',
  `create_user` varchar(36)  NULL COMMENT '作成ユーザーID',
  `update_date` timestamp(0) NULL COMMENT '更新日時',
  `update_user` varchar(36)  NULL COMMENT '更新ユーザーID',
  PRIMARY KEY (`user_id`) USING BTREE,
  INDEX `tenant_id`(`tenant_id`) USING BTREE,
  CONSTRAINT `m_user_ibfk_1` FOREIGN KEY (`tenant_id`) REFERENCES `m_tenant` (`tenant_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ;

-- ----------------------------
-- Table structure for m_user_store_authority
-- ----------------------------
DROP TABLE IF EXISTS `m_user_store_authority`;
CREATE TABLE `m_user_store_authority`  (
  `tenant_id` int(11) NOT NULL,
  `user_store_authority_id` varchar(36)  ,
  `store_id` varchar(36) COMMENT '１：編集可能　　２：参照可能　　３：参照不可',
  `user_id` varchar(36)  ,
  `authority` smallint(1) NOT NULL,
  `create_date` timestamp(0) NULL COMMENT '作成日時',
  `create_user` varchar(36)  NULL COMMENT '作成ユーザーID',
  `update_date` timestamp(0) NULL COMMENT '更新日時',
  `update_user` varchar(36)  NULL COMMENT '更新ユーザーID',
  PRIMARY KEY (`user_store_authority_id`) USING BTREE,
  INDEX `store_id`(`store_id`) USING BTREE,
  INDEX `tenant_id`(`tenant_id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
  CONSTRAINT `m_user_store_authority_ibfk_1` FOREIGN KEY (`store_id`) REFERENCES `m_stores` (`storeid`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `m_user_store_authority_ibfk_2` FOREIGN KEY (`tenant_id`) REFERENCES `m_tenant` (`tenant_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `m_user_store_authority_ibfk_3` FOREIGN KEY (`user_id`) REFERENCES `m_user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ;

-- ----------------------------
-- Table structure for t_delivery
-- ----------------------------
DROP TABLE IF EXISTS `t_delivery`;
CREATE TABLE `t_delivery`  (
  `tenant_id` int(11) NOT NULL,
  `order_id` varchar(36)  NULL COMMENT '荷姿ID',
  `delivery_id` varchar(36) COMMENT '実績ID',
  `create_date` timestamp(0) NULL COMMENT '作成日時',
  `create_user` varchar(36)  NULL COMMENT '作成ユーザーID',
  `update_date` timestamp(0) NULL COMMENT '更新日時',
  `update_user` varchar(36)  NULL COMMENT '更新ユーザーID',
  PRIMARY KEY (`delivery_id`) USING BTREE,
  INDEX `tenant_id`(`tenant_id`) USING BTREE,
  INDEX `order_id`(`order_id`) USING BTREE,
  CONSTRAINT `t_delivery_ibfk_1` FOREIGN KEY (`tenant_id`) REFERENCES `m_tenant` (`tenant_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `t_delivery_ibfk_2` FOREIGN KEY (`order_id`) REFERENCES `t_order` (`order_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ;

-- ----------------------------
-- Table structure for t_delivery_detail
-- ----------------------------
DROP TABLE IF EXISTS `t_delivery_detail`;
CREATE TABLE `t_delivery_detail`  (
  `tenant_id` int(11) NOT NULL,
  `delivery_detail_id` varchar(36)  ,
  `delivery_id` varchar(36)  ,
  `package_id` varchar(36)  ,
  `amount` decimal(11, 0) NULL,
  `create_date` timestamp(0) NULL COMMENT '作成日時',
  `create_user` varchar(36)  NULL COMMENT '作成ユーザーID',
  `update_date` timestamp(0) NULL COMMENT '更新日時',
  `update_user` varchar(36)  NULL COMMENT '更新ユーザーID',
  PRIMARY KEY (`delivery_detail_id`) USING BTREE,
  INDEX `tenant_id`(`tenant_id`) USING BTREE,
  INDEX `delivery_id`(`delivery_id`) USING BTREE,
  INDEX `package_id`(`package_id`) USING BTREE,
  CONSTRAINT `t_delivery_detail_ibfk_1` FOREIGN KEY (`tenant_id`) REFERENCES `m_tenant` (`tenant_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `t_delivery_detail_ibfk_2` FOREIGN KEY (`delivery_id`) REFERENCES `t_delivery` (`delivery_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `t_delivery_detail_ibfk_3` FOREIGN KEY (`package_id`) REFERENCES `m_package` (`pakage_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ;

-- ----------------------------
-- Table structure for t_logicstic
-- ----------------------------
DROP TABLE IF EXISTS `t_logicstics`;
CREATE TABLE `t_logicstics`  (
  `tenant_id` int(11) NOT NULL,
  `logistics_id` varchar(36) COMMENT '物流ID',
  `order_id` varchar(36) COMMENT '発注ID',
  `create_date` timestamp(0) NULL COMMENT '作成日時',
  `create_user` varchar(36)  NULL COMMENT '作成ユーザーID',
  `update_date` timestamp(0) NULL COMMENT '更新日時',
  `update_user` varchar(36)  NULL COMMENT '更新ユーザーID',
  PRIMARY KEY (`logistics_id`) USING BTREE,
  INDEX `tenant_id`(`tenant_id`) USING BTREE,
  INDEX `order_id`(`order_id`) USING BTREE,
  CONSTRAINT `t_logicstics_ibfk_1` FOREIGN KEY (`tenant_id`) REFERENCES `m_tenant` (`tenant_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `t_logicstics_ibfk_2` FOREIGN KEY (`order_id`) REFERENCES `t_order` (`order_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ;

-- ----------------------------
-- Table structure for t_logistic_detail
-- ----------------------------
DROP TABLE IF EXISTS `t_logistics_detail`;
CREATE TABLE `t_logistics_detail`  (
  `tenant_id` int(11) NOT NULL,
  `logistics_detail_id` varchar(11) COMMENT 'ID',
  `logicstics_id` varchar(36)  NULL COMMENT '物流ID',
  `package_id` varchar(36)  NULL COMMENT '荷姿ID',
  `amount` decimal(11, 0) NULL COMMENT '個数',
  `create_date` timestamp(0) NULL COMMENT '作成日時',
  `create_user` varchar(36)  NULL COMMENT '作成ユーザーID',
  `update_date` timestamp(0) NULL COMMENT '更新日時',
  `update_user` varchar(36)  NULL COMMENT '更新ユーザーID',
  PRIMARY KEY (`logistics_detail_id`) USING BTREE,
  INDEX `logicstics_id`(`logicstics_id`) USING BTREE,
  INDEX `tenant_id`(`tenant_id`) USING BTREE,
  INDEX `package_id`(`package_id`) USING BTREE,
  CONSTRAINT `t_logistics_detail_ibfk_1` FOREIGN KEY (`logicstics_id`) REFERENCES `t_logicstics` (`logistics_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `t_logistics_detail_ibfk_2` FOREIGN KEY (`tenant_id`) REFERENCES `m_tenant` (`tenant_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `t_logistics_detail_ibfk_3` FOREIGN KEY (`package_id`) REFERENCES `m_package` (`pakage_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ;

-- ----------------------------
-- Table structure for t_order
-- ----------------------------
DROP TABLE IF EXISTS `t_order`;
CREATE TABLE `t_order`  (
  `tenant_id` int(11) NOT NULL,
  `order_id` varchar(36) COMMENT '発注ID',
  `order_date` datetime(0) NULL,
  `store_id` varchar(11)  NULL COMMENT '店舗ID',
  `post` varchar(50)  NULL COMMENT '便',
  `create_date` timestamp(0) NULL COMMENT '作成日時',
  `create_user` varchar(36)  NULL COMMENT '作成ユーザーID',
  `update_date` timestamp(0) NULL COMMENT '更新日時',
  `update_user` varchar(36)  NULL COMMENT '更新ユーザーID',
  PRIMARY KEY (`order_id`) USING BTREE,
  INDEX `tenant_id`(`tenant_id`) USING BTREE,
  INDEX `store_id`(`store_id`) USING BTREE,
  INDEX `order_id`(`order_id`) USING BTREE,
  INDEX `order_date`(`order_date`) USING BTREE,
  INDEX `post`(`post`) USING BTREE,
  CONSTRAINT `t_order_ibfk_1` FOREIGN KEY (`tenant_id`) REFERENCES `m_tenant` (`tenant_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `t_order_ibfk_2` FOREIGN KEY (`store_id`) REFERENCES `m_stores` (`storeid`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ;

-- ----------------------------
-- Table structure for t_order_detail
-- ----------------------------
DROP TABLE IF EXISTS `t_order_detail`;
CREATE TABLE `t_order_detail`  (
  `tenant_id` int(11) NOT NULL,
  `order_detail_id` varchar(36) COMMENT '発注詳細ID',
  `order_id` varchar(36) COMMENT '発注ID',
  `product_id` varchar(36) COMMENT '商品ID',
  `amount` decimal(11, 0) NULL COMMENT '個数',
  `create_date` timestamp(0) NULL COMMENT '作成日時',
  `create_user` varchar(36)  NULL COMMENT '作成ユーザーID',
  `update_date` timestamp(0) NULL COMMENT '更新日時',
  `update_user` varchar(36)  NULL COMMENT '更新ユーザーID',
  PRIMARY KEY (`order_detail_id`) USING BTREE,
  INDEX `tenant_id`(`tenant_id`) USING BTREE,
  INDEX `product_id`(`product_id`) USING BTREE,
  INDEX `t_order_detail_ibfk_3`(`order_id`) USING BTREE,
  CONSTRAINT `t_order_detail_ibfk_1` FOREIGN KEY (`tenant_id`) REFERENCES `m_tenant` (`tenant_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `t_order_detail_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `m_product` (`product_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `t_order_detail_ibfk_3` FOREIGN KEY (`order_id`) REFERENCES `t_order` (`order_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ;

SET FOREIGN_KEY_CHECKS = 1;