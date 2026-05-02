ALTER TABLE `prop_info`
  CHANGE COLUMN `fire_rating` `fire_resistant_option` VARCHAR(32) DEFAULT NULL COMMENT '是否阻燃：是/否/可喷阻燃液',
  CHANGE COLUMN `transport_requirement` `weight_desc` VARCHAR(255) DEFAULT NULL COMMENT '重量描述，如 25kg / 80kg';
