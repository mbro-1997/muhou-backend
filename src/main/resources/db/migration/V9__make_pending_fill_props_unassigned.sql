ALTER TABLE `prop_info`
  MODIFY COLUMN `supplier_user_id` BIGINT NULL COMMENT '所属工厂用户ID，管理员生成二维码时为空，工厂扫码录入后再绑定';

UPDATE `prop_info`
SET `supplier_user_id` = NULL
WHERE `fill_status` = 'pending_fill';
