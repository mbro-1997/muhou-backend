CREATE TABLE IF NOT EXISTS `sys_user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户主键ID',
  `nickname` VARCHAR(64) NOT NULL COMMENT '用户昵称',
  `avatar_url` VARCHAR(255) DEFAULT NULL COMMENT '用户头像地址',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
  `realname_verified` TINYINT NOT NULL DEFAULT 0 COMMENT '是否已实名认证：0否 1是',
  `student_verified` TINYINT NOT NULL DEFAULT 0 COMMENT '是否已学生认证：0否 1是',
  `user_status` VARCHAR(20) NOT NULL DEFAULT 'active' COMMENT '用户状态：active正常 disabled停用 suspended冻结',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户表';

CREATE TABLE IF NOT EXISTS `sys_user_role` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户角色绑定主键ID',
  `user_id` BIGINT NOT NULL COMMENT '关联用户ID',
  `role_code` VARCHAR(20) NOT NULL COMMENT '角色编码：demander需求方 supplier工厂 admin管理员',
  `enabled` TINYINT NOT NULL DEFAULT 1 COMMENT '是否启用：0否 1是',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_sys_user_role` (`user_id`, `role_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色绑定表';

CREATE TABLE IF NOT EXISTS `supplier_settlement_audit` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '工厂入驻审核主键ID',
  `user_id` BIGINT NOT NULL COMMENT '申请入驻的用户ID',
  `company_name` VARCHAR(128) NOT NULL COMMENT '工厂名称',
  `contact_name` VARCHAR(64) NOT NULL COMMENT '联系人',
  `contact_phone` VARCHAR(20) NOT NULL COMMENT '联系电话',
  `audit_status` VARCHAR(20) NOT NULL DEFAULT 'pending' COMMENT '审核状态：pending待审核 approved通过 rejected驳回',
  `audit_remark` VARCHAR(255) DEFAULT NULL COMMENT '审核备注',
  `reviewer_id` BIGINT DEFAULT NULL COMMENT '审核人用户ID',
  `submitted_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '提交时间',
  `reviewed_at` DATETIME DEFAULT NULL COMMENT '审核时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='工厂入驻审核表';

CREATE TABLE IF NOT EXISTS `prop_audit` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '道具审核主键ID',
  `prop_id` BIGINT NOT NULL COMMENT '关联道具ID',
  `action_type` VARCHAR(20) NOT NULL COMMENT '审核动作：create新增 up上架 down下架',
  `audit_status` VARCHAR(20) NOT NULL DEFAULT 'pending' COMMENT '审核状态：pending待审核 approved通过 rejected驳回',
  `apply_remark` VARCHAR(255) DEFAULT NULL COMMENT '申请说明',
  `audit_remark` VARCHAR(255) DEFAULT NULL COMMENT '审核备注',
  `reviewer_id` BIGINT DEFAULT NULL COMMENT '审核人用户ID',
  `submitted_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '提交时间',
  `reviewed_at` DATETIME DEFAULT NULL COMMENT '审核时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='道具审核记录表';

CREATE TABLE IF NOT EXISTS `order_dispute` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '纠纷主键ID',
  `order_id` BIGINT NOT NULL COMMENT '关联订单ID',
  `apply_user_id` BIGINT NOT NULL COMMENT '发起纠纷用户ID',
  `title` VARCHAR(128) NOT NULL COMMENT '纠纷标题',
  `content` VARCHAR(2000) NOT NULL COMMENT '纠纷描述',
  `dispute_status` VARCHAR(20) NOT NULL DEFAULT 'pending' COMMENT '纠纷状态：pending待处理 resolved已裁定 closed已关闭',
  `resolution` VARCHAR(2000) DEFAULT NULL COMMENT '裁定结果',
  `reviewer_id` BIGINT DEFAULT NULL COMMENT '裁定管理员ID',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `resolved_at` DATETIME DEFAULT NULL COMMENT '裁定时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='纠纷仲裁表';

INSERT INTO `sys_user` (`id`, `nickname`, `avatar_url`, `phone`, `realname_verified`, `student_verified`, `user_status`, `created_at`, `updated_at`) VALUES
  (1, '需求方测试账号', '/images/avatar.png', '13800000001', 1, 1, 'active', NOW(), NOW()),
  (2, '工厂测试账号', '/images/avatar.png', '13800000002', 1, 1, 'active', NOW(), NOW()),
  (3, '管理员测试账号', '/images/avatar.png', '13800000003', 1, 1, 'active', NOW(), NOW()),
  (4, '星幕舞美工厂', '/images/avatar.png', '13800000004', 1, 0, 'active', NOW(), NOW()),
  (5, '云启道具工作室', '/images/avatar.png', '13800000005', 1, 0, 'active', NOW(), NOW())
ON DUPLICATE KEY UPDATE
  `nickname` = VALUES(`nickname`),
  `avatar_url` = VALUES(`avatar_url`),
  `phone` = VALUES(`phone`),
  `realname_verified` = VALUES(`realname_verified`),
  `student_verified` = VALUES(`student_verified`),
  `user_status` = VALUES(`user_status`),
  `updated_at` = NOW();

INSERT INTO `sys_user_role` (`user_id`, `role_code`, `enabled`, `created_at`) VALUES
  (1, 'demander', 1, NOW()),
  (2, 'supplier', 1, NOW()),
  (3, 'admin', 1, NOW()),
  (4, 'supplier', 1, NOW()),
  (5, 'supplier', 1, NOW())
ON DUPLICATE KEY UPDATE
  `enabled` = VALUES(`enabled`);

INSERT INTO `supplier_settlement_audit` (`id`, `user_id`, `company_name`, `contact_name`, `contact_phone`, `audit_status`, `audit_remark`, `reviewer_id`, `submitted_at`, `reviewed_at`) VALUES
  (1, 4, '星幕舞美工厂', '李师傅', '13800000004', 'pending', NULL, NULL, DATE_SUB(NOW(), INTERVAL 2 DAY), NULL),
  (2, 5, '云启道具工作室', '陈经理', '13800000005', 'pending', NULL, NULL, DATE_SUB(NOW(), INTERVAL 1 DAY), NULL)
ON DUPLICATE KEY UPDATE
  `company_name` = VALUES(`company_name`),
  `contact_name` = VALUES(`contact_name`),
  `contact_phone` = VALUES(`contact_phone`),
  `audit_status` = VALUES(`audit_status`),
  `audit_remark` = VALUES(`audit_remark`),
  `reviewer_id` = VALUES(`reviewer_id`),
  `submitted_at` = VALUES(`submitted_at`),
  `reviewed_at` = VALUES(`reviewed_at`);

INSERT INTO `prop_audit` (`id`, `prop_id`, `action_type`, `audit_status`, `apply_remark`, `audit_remark`, `reviewer_id`, `submitted_at`, `reviewed_at`) VALUES
  (1, 1, 'up', 'pending', '申请上架舞台聚光灯', NULL, NULL, DATE_SUB(NOW(), INTERVAL 12 HOUR), NULL),
  (2, 5, 'down', 'pending', '申请下架背景幕布', NULL, NULL, DATE_SUB(NOW(), INTERVAL 8 HOUR), NULL)
ON DUPLICATE KEY UPDATE
  `prop_id` = VALUES(`prop_id`),
  `action_type` = VALUES(`action_type`),
  `audit_status` = VALUES(`audit_status`),
  `apply_remark` = VALUES(`apply_remark`),
  `audit_remark` = VALUES(`audit_remark`),
  `reviewer_id` = VALUES(`reviewer_id`),
  `submitted_at` = VALUES(`submitted_at`),
  `reviewed_at` = VALUES(`reviewed_at`);

INSERT INTO `order_dispute` (`id`, `order_id`, `apply_user_id`, `title`, `content`, `dispute_status`, `resolution`, `reviewer_id`, `created_at`, `resolved_at`) VALUES
  (1, 1002, 1, '道具破损责任争议', '需求方认为收到时就有轻微损坏，工厂认为使用中造成，需要管理员介入裁定。', 'pending', NULL, NULL, DATE_SUB(NOW(), INTERVAL 6 HOUR), NULL)
ON DUPLICATE KEY UPDATE
  `order_id` = VALUES(`order_id`),
  `apply_user_id` = VALUES(`apply_user_id`),
  `title` = VALUES(`title`),
  `content` = VALUES(`content`),
  `dispute_status` = VALUES(`dispute_status`),
  `resolution` = VALUES(`resolution`),
  `reviewer_id` = VALUES(`reviewer_id`),
  `created_at` = VALUES(`created_at`),
  `resolved_at` = VALUES(`resolved_at`);

INSERT INTO `order_review` (`id`, `order_id`, `reviewer_user_id`, `reviewer_role`, `score`, `content`, `visible_flag`, `created_at`) VALUES
  (1, 1002, 1, 'demander', 5, '设备成色很好，交付也很准时。', 1, DATE_SUB(NOW(), INTERVAL 3 HOUR)),
  (2, 1001, 2, 'supplier', 4, '需求沟通比较顺畅，但希望后续提前确认使用时段。', 1, DATE_SUB(NOW(), INTERVAL 2 HOUR))
ON DUPLICATE KEY UPDATE
  `score` = VALUES(`score`),
  `content` = VALUES(`content`),
  `visible_flag` = VALUES(`visible_flag`),
  `created_at` = VALUES(`created_at`);

UPDATE `prop_info`
SET
  `prop_name` = '舞台聚光灯',
  `style_code` = '现代',
  `type_code` = '灯光',
  `material_desc` = '金属',
  `transport_requirement` = '防震运输',
  `transport_suggestion` = '厢式货车',
  `remark` = '演示数据'
WHERE `id` = 1;

UPDATE `prop_info`
SET
  `prop_name` = '专业音响系统',
  `style_code` = '现代',
  `type_code` = '音响',
  `material_desc` = '塑料+金属',
  `transport_requirement` = '立放固定',
  `transport_suggestion` = '中型货车',
  `remark` = '演示数据'
WHERE `id` = 2;

UPDATE `prop_info`
SET
  `prop_name` = 'LED显示屏',
  `style_code` = '科技',
  `type_code` = '屏幕',
  `material_desc` = 'LED+金属',
  `transport_requirement` = '防震木箱',
  `transport_suggestion` = '厢式货车',
  `remark` = '演示数据'
WHERE `id` = 3;

UPDATE `prop_info`
SET
  `prop_name` = '舞台桁架',
  `style_code` = '工业',
  `type_code` = '结构',
  `material_desc` = '铝合金',
  `transport_requirement` = '捆扎运输',
  `transport_suggestion` = '平板车',
  `remark` = '演示数据'
WHERE `id` = 4;

UPDATE `prop_info`
SET
  `prop_name` = '背景幕布',
  `style_code` = '传统',
  `type_code` = '布景',
  `material_desc` = '阻燃布',
  `transport_requirement` = '卷装运输',
  `transport_suggestion` = '小型货车',
  `remark` = '演示数据'
WHERE `id` = 5;

UPDATE `prop_info`
SET
  `prop_name` = '无线麦克风套装',
  `style_code` = '现代',
  `type_code` = '音响',
  `material_desc` = '工程塑料',
  `transport_requirement` = '防摔箱',
  `transport_suggestion` = '小型货车',
  `remark` = '演示数据'
WHERE `id` = 6;

UPDATE `project_scheme`
SET
  `project_name` = '毕业晚会方案',
  `project_description` = '校园舞台项目'
WHERE `id` = 1;

UPDATE `rental_order`
SET
  `remark` = CASE
    WHEN `id` = 1001 THEN '演示待确认订单'
    WHEN `id` = 1002 THEN '演示租赁中订单'
    ELSE `remark`
  END
WHERE `id` IN (1001, 1002);

UPDATE `rental_order_item`
SET
  `prop_name_snapshot` = CASE
    WHEN `prop_id` = 2 THEN '专业音响系统'
    WHEN `prop_id` = 3 THEN 'LED显示屏'
    WHEN `prop_id` = 4 THEN '舞台桁架'
    ELSE `prop_name_snapshot`
  END
WHERE `prop_id` IN (2, 3, 4);
