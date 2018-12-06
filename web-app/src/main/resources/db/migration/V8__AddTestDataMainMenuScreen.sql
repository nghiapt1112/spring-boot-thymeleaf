SET FOREIGN_KEY_CHECKS=0;

DELETE FROM t_order;
DELETE FROM t_order_detail;
DELETE FROM t_logistics;
DELETE FROM t_logistics_detail;

ALTER TABLE t_order
  DROP FOREIGN KEY `t_order_ibfk_2`;
alter table t_order drop  column store_id;
alter table t_order drop column  post;

alter table t_order add post_course_id varchar(36) not null after order_id;
ALTER TABLE t_order ADD CONSTRAINT t_order_ibfk_2 FOREIGN KEY (post_course_id) REFERENCES `m_post_course` (`post_course_id`) ON DELETE RESTRICT ON UPDATE RESTRICT;

SET FOREIGN_KEY_CHECKS=1;