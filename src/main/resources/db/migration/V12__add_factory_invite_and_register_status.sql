ALTER TABLE `sys_user`
  ADD COLUMN `register_status` VARCHAR(32) NOT NULL DEFAULT 'active' COMMENT '注册状态：new/active/factory_pending/factory_rejected/disabled' AFTER `user_status`;

UPDATE `sys_user`
SET `register_status` = CASE
  WHEN `user_status` = 'disabled' THEN 'disabled'
  ELSE 'active'
END
WHERE `register_status` IS NULL OR `register_status` = '';

CREATE TABLE IF NOT EXISTS `factory_invite_code` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '邀请码主键ID',
  `code_hash` VARCHAR(128) NOT NULL COMMENT '邀请码哈希值',
  `code_suffix` VARCHAR(16) NOT NULL COMMENT '邀请码后缀',
  `status` VARCHAR(20) NOT NULL DEFAULT 'unused' COMMENT '状态：unused/locked/used/expired/revoked',
  `expire_at` DATETIME NOT NULL COMMENT '过期时间',
  `created_by_admin_user_id` BIGINT NOT NULL COMMENT '创建管理员用户ID',
  `locked_by_user_id` BIGINT DEFAULT NULL COMMENT '锁定用户ID',
  `locked_at` DATETIME DEFAULT NULL COMMENT '锁定时间',
  `used_by_user_id` BIGINT DEFAULT NULL COMMENT '使用用户ID',
  `used_at` DATETIME DEFAULT NULL COMMENT '使用时间',
  `revoked_by_admin_user_id` BIGINT DEFAULT NULL COMMENT '作废管理员用户ID',
  `revoked_at` DATETIME DEFAULT NULL COMMENT '作废时间',
  `remark` VARCHAR(255) DEFAULT NULL COMMENT '邀请码备注',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_factory_invite_code_hash` (`code_hash`),
  KEY `idx_factory_invite_code_status` (`status`),
  KEY `idx_factory_invite_code_suffix` (`code_suffix`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='工厂入驻邀请码表';

CREATE TABLE IF NOT EXISTS `factory_profile` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '工厂主体主键ID',
  `owner_user_id` BIGINT NOT NULL COMMENT '工厂所属用户ID',
  `factory_name` VARCHAR(128) NOT NULL COMMENT '工厂名称',
  `unified_social_credit_code` VARCHAR(32) NOT NULL COMMENT '统一社会信用代码',
  `business_license_url` VARCHAR(255) NOT NULL COMMENT '营业执照图片地址',
  `contact_name` VARCHAR(64) NOT NULL COMMENT '联系人姓名',
  `contact_phone` VARCHAR(20) NOT NULL COMMENT '联系人手机号',
  `factory_address` VARCHAR(255) NOT NULL COMMENT '工厂地址',
  `main_business` VARCHAR(255) DEFAULT NULL COMMENT '主营业务',
  `status` VARCHAR(20) NOT NULL DEFAULT 'active' COMMENT '工厂状态：active/disabled',
  `approved_audit_id` BIGINT DEFAULT NULL COMMENT '审核通过的入驻审核ID',
  `approved_by_admin_user_id` BIGINT DEFAULT NULL COMMENT '审核通过管理员ID',
  `approved_at` DATETIME DEFAULT NULL COMMENT '审核通过时间',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_factory_profile_owner` (`owner_user_id`),
  UNIQUE KEY `uk_factory_profile_credit_code` (`unified_social_credit_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='正式工厂主体表';

ALTER TABLE `supplier_settlement_audit`
  ADD COLUMN `applicant_user_id` BIGINT DEFAULT NULL COMMENT '申请用户ID' AFTER `user_id`,
  ADD COLUMN `invite_code_id` BIGINT DEFAULT NULL COMMENT '邀请码ID' AFTER `applicant_user_id`,
  ADD COLUMN `factory_name` VARCHAR(128) DEFAULT NULL COMMENT '工厂名称' AFTER `company_name`,
  ADD COLUMN `unified_social_credit_code` VARCHAR(32) DEFAULT NULL COMMENT '统一社会信用代码' AFTER `factory_name`,
  ADD COLUMN `business_license_url` VARCHAR(255) DEFAULT NULL COMMENT '营业执照图片地址' AFTER `unified_social_credit_code`,
  ADD COLUMN `factory_address` VARCHAR(255) DEFAULT NULL COMMENT '工厂地址' AFTER `contact_phone`,
  ADD COLUMN `main_business` VARCHAR(255) DEFAULT NULL COMMENT '主营道具类型' AFTER `factory_address`,
  ADD COLUMN `remark` VARCHAR(255) DEFAULT NULL COMMENT '申请备注' AFTER `main_business`,
  ADD COLUMN `reject_reason` VARCHAR(255) DEFAULT NULL COMMENT '驳回原因' AFTER `audit_remark`,
  ADD COLUMN `reviewed_by_admin_user_id` BIGINT DEFAULT NULL COMMENT '审核管理员ID' AFTER `reviewer_id`,
  ADD COLUMN `created_factory_id` BIGINT DEFAULT NULL COMMENT '创建后的工厂主体ID' AFTER `reviewed_at`,
  ADD COLUMN `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间' AFTER `created_factory_id`;

UPDATE `supplier_settlement_audit`
SET `applicant_user_id` = COALESCE(`applicant_user_id`, `user_id`),
    `factory_name` = COALESCE(NULLIF(`factory_name`, ''), `company_name`),
    `reviewed_by_admin_user_id` = COALESCE(`reviewed_by_admin_user_id`, `reviewer_id`),
    `updated_at` = NOW();

INSERT INTO `sys_user` (`id`, `nickname`, `avatar_url`, `phone`, `realname_verified`, `student_verified`, `user_status`, `register_status`, `created_at`, `updated_at`)
VALUES (6, '新用户演示账号', '/images/avatar.png', '13800000006', 0, 0, 'active', 'new', NOW(), NOW())
ON DUPLICATE KEY UPDATE
  `nickname` = VALUES(`nickname`),
  `avatar_url` = VALUES(`avatar_url`),
  `phone` = VALUES(`phone`),
  `realname_verified` = VALUES(`realname_verified`),
  `student_verified` = VALUES(`student_verified`),
  `user_status` = VALUES(`user_status`),
  `register_status` = VALUES(`register_status`),
  `updated_at` = NOW();

INSERT INTO `sys_user_role` (`user_id`, `role_code`, `enabled`, `created_at`)
VALUES (3, 'supplier', 1, NOW())
ON DUPLICATE KEY UPDATE
  `enabled` = VALUES(`enabled`);

INSERT INTO `sys_user_wechat` (`user_id`, `openid`, `unionid`, `session_key`, `created_at`, `updated_at`)
VALUES
  (1, 'demo-openid-demander', NULL, 'demo-session-demander', NOW(), NOW()),
  (2, 'demo-openid-supplier', NULL, 'demo-session-supplier', NOW(), NOW()),
  (3, 'demo-openid-admin', NULL, 'demo-session-admin', NOW(), NOW()),
  (6, 'demo-openid-new-user', NULL, 'demo-session-new-user', NOW(), NOW())
ON DUPLICATE KEY UPDATE
  `session_key` = VALUES(`session_key`),
  `updated_at` = NOW();
