ALTER TABLE `rental_order`
  ADD COLUMN `contact_address` VARCHAR(255) NULL COMMENT '租赁方本次订单收货/使用地址' AFTER `rental_days`,
  ADD COLUMN `use_scene` VARCHAR(100) NULL COMMENT '本次租赁使用场景' AFTER `contact_address`,
  ADD COLUMN `special_remark` VARCHAR(500) NULL COMMENT '本次订单特殊用途或备注' AFTER `use_scene`;

ALTER TABLE `sys_user`
  ADD COLUMN `credit_score` INT NOT NULL DEFAULT 80 COMMENT '用户信用值，工厂查看订单时展示' AFTER `student_verified`;

UPDATE `sys_user`
SET `credit_score` = CASE
  WHEN `student_verified` = 1 THEN 98
  WHEN `realname_verified` = 1 THEN 90
  ELSE 80
END
WHERE `credit_score` = 80;
