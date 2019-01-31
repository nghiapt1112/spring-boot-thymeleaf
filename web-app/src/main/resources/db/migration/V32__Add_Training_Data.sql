DROP TABLE IF EXISTS `t_training_data`;
CREATE TABLE `t_training_data`  (
  `tenant_id` int(11) NOT NULL,
  `training_id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `order_id` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '荷姿ID',
  `input_item` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `output_item` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `create_date` timestamp(0) NULL DEFAULT NULL COMMENT '作成日時',
  `create_user` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '作成ユーザーID',
  `update_date` timestamp(0) NULL DEFAULT NULL COMMENT '更新日時',
  `update_user` varchar(36) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更新ユーザーID',
  PRIMARY KEY (`training_id`) USING BTREE,
  INDEX `tenant_id`(`tenant_id`) USING BTREE,
  INDEX `order_id`(`order_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Dynamic;