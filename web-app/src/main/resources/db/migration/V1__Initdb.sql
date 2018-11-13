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
  `name` varchar(255)   COMMENT '名称\r\n',
  `user_limit_number` int(5) NOT NULL,
  `create_date` timestamp(0) NULL DEFAULT NULL COMMENT '作成日時',
  `create_user` varchar(36)  NULL DEFAULT NULL COMMENT '作成ユーザーID',
  `update_date` timestamp(0) NULL DEFAULT NULL COMMENT '更新日時',
  `update_user` varchar(36)  NULL DEFAULT NULL COMMENT '更新ユーザーID',
  PRIMARY KEY (`tenant_id`) USING BTREE
) ;

-- ----------------------------
-- Table structure for m_user_store_authority
-- ----------------------------
DROP TABLE IF EXISTS `m_user_store_authority`;
CREATE TABLE `m_user_store_authority`  (
  `tenant_id` int(11) NOT NULL,
  `user_store_authority_id` varchar(36)  ,
  `store_id` varchar(36)   COMMENT '１：編集可能　　２：参照可能　　３：参照不可',
  `user_id` varchar(36)  ,
  `authority` smallint(1) NOT NULL,
  `create_date` timestamp(0) NULL DEFAULT NULL COMMENT '作成日時',
  `create_user` varchar(36)  NULL DEFAULT NULL COMMENT '作成ユーザーID',
  `update_date` timestamp(0) NULL DEFAULT NULL COMMENT '更新日時',
  `update_user` varchar(36)  NULL DEFAULT NULL COMMENT '更新ユーザーID',
  PRIMARY KEY (`user_store_authority_id`) USING BTREE,
  INDEX `store_id`(`store_id`) USING BTREE,
  INDEX `tenant_id`(`tenant_id`) USING BTREE,
  INDEX `user_id`(`user_id`) USING BTREE,
--   CONSTRAINT `m_user_store_authority_ibfk_1` FOREIGN KEY (`store_id`) REFERENCES `m_stores` (`storeid`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `m_user_store_authority_ibfk_2` FOREIGN KEY (`tenant_id`) REFERENCES `m_tenant` (`tenant_id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `m_user_store_authority_ibfk_3` FOREIGN KEY (`user_id`) REFERENCES `m_user` (`user_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ;


-- ----------------------------
-- Table structure for m_user
-- ----------------------------
DROP TABLE IF EXISTS `m_user`;
CREATE TABLE `m_user`  (
  `tenant_id` int(11) NOT NULL COMMENT 'テナントID\r\n',
  `user_id` varchar(36)   COMMENT 'ID',
  `email` varchar(255)   COMMENT 'メール',
  `password` varchar(100)   COMMENT 'パスワード',
  `name` varchar(255)  NULL DEFAULT NULL COMMENT '名前',
  `role` smallint(2) NULL DEFAULT NULL COMMENT '権限',
  `create_date` timestamp(0) NULL DEFAULT NULL COMMENT '作成日時',
  `create_user` varchar(36)  NULL DEFAULT NULL COMMENT '作成ユーザーID',
  `update_date` timestamp(0) NULL DEFAULT NULL COMMENT '更新日時',
  `update_user` varchar(36)  NULL DEFAULT NULL COMMENT '更新ユーザーID',
  PRIMARY KEY (`user_id`) USING BTREE,
  INDEX `tenant_id`(`tenant_id`) USING BTREE,
  CONSTRAINT `m_user_ibfk_1` FOREIGN KEY (`tenant_id`) REFERENCES `m_tenant` (`tenant_id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ;

SET FOREIGN_KEY_CHECKS = 1;