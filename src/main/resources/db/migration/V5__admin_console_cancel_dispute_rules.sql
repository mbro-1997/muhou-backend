ALTER TABLE `rental_order`
  ADD COLUMN `cancel_type` VARCHAR(64) DEFAULT NULL COMMENT 'cancel type' AFTER `cancel_reason`;

ALTER TABLE `order_dispute`
  ADD COLUMN `reason_code` VARCHAR(64) DEFAULT NULL COMMENT 'dispute reason code' AFTER `apply_stage`,
  ADD COLUMN `reason_label` VARCHAR(128) DEFAULT NULL COMMENT 'dispute reason label' AFTER `reason_code`,
  ADD COLUMN `admin_decision_amount_fen` INT DEFAULT NULL COMMENT 'admin decision amount in fen' AFTER `claim_amount_fen`,
  ADD COLUMN `fund_effect_status` VARCHAR(32) NOT NULL DEFAULT 'none' COMMENT 'none/pending_settlement/executed' AFTER `refund_status`,
  ADD COLUMN `admin_action_type` VARCHAR(64) DEFAULT NULL COMMENT 'admin decision action type' AFTER `fund_effect_status`;

CREATE TABLE `admin_account` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL COMMENT 'sys_user.id',
  `username` VARCHAR(64) NOT NULL COMMENT 'admin web username',
  `password_hash` VARCHAR(255) NOT NULL COMMENT 'BCrypt password hash',
  `enabled` TINYINT NOT NULL DEFAULT 1,
  `last_login_at` DATETIME DEFAULT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_admin_account_username` (`username`),
  UNIQUE KEY `uk_admin_account_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='admin web account';

CREATE TABLE `order_admin_action` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `order_id` BIGINT NOT NULL,
  `dispute_id` BIGINT DEFAULT NULL,
  `action_type` VARCHAR(64) NOT NULL,
  `action_status` VARCHAR(32) NOT NULL DEFAULT 'done',
  `before_order_status` VARCHAR(32) DEFAULT NULL,
  `after_order_status` VARCHAR(32) DEFAULT NULL,
  `before_pay_status` VARCHAR(32) DEFAULT NULL,
  `after_pay_status` VARCHAR(32) DEFAULT NULL,
  `amount_fen` INT DEFAULT NULL,
  `target_role` VARCHAR(32) DEFAULT NULL,
  `target_user_id` BIGINT DEFAULT NULL,
  `score_delta` INT DEFAULT NULL,
  `reason` VARCHAR(500) DEFAULT NULL,
  `internal_note` VARCHAR(1000) DEFAULT NULL,
  `operator_user_id` BIGINT NOT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_order_admin_action_order` (`order_id`, `created_at`),
  KEY `idx_order_admin_action_dispute` (`dispute_id`),
  KEY `idx_order_admin_action_type` (`action_type`, `created_at`),
  KEY `idx_order_admin_action_target` (`target_role`, `target_user_id`, `created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='order admin action log';

CREATE TABLE `dispute_reason_config` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `stage` VARCHAR(32) NOT NULL,
  `applicant_role` VARCHAR(20) NOT NULL,
  `reason_code` VARCHAR(64) NOT NULL,
  `reason_label` VARCHAR(128) NOT NULL,
  `description` VARCHAR(255) DEFAULT NULL,
  `require_amount` TINYINT NOT NULL DEFAULT 1,
  `require_images` TINYINT NOT NULL DEFAULT 0,
  `enabled` TINYINT NOT NULL DEFAULT 1,
  `sort_order` INT NOT NULL DEFAULT 0,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_dispute_reason` (`stage`, `applicant_role`, `reason_code`),
  KEY `idx_dispute_reason_query` (`stage`, `applicant_role`, `enabled`, `sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='dispute reason config';

INSERT INTO `dispute_reason_config`
(`stage`, `applicant_role`, `reason_code`, `reason_label`, `description`, `require_amount`, `require_images`, `enabled`, `sort_order`)
VALUES
('renting', 'demander', 'goods_not_match', '货不对板', '实际到货与道具详情描述明显不一致', 1, 0, 1, 10),
('renting', 'demander', 'size_or_spec_mismatch', '尺寸/规格不符', '尺寸、规格、接口等影响正常使用', 1, 0, 1, 20),
('renting', 'demander', 'initial_damage', '到货已有损坏', '到货时已存在损坏或明显瑕疵', 1, 1, 1, 30),
('renting', 'demander', 'cannot_use', '影响正常使用', '道具无法按约定场景正常使用', 1, 0, 1, 40),
('renting', 'demander', 'delivery_issue', '交付问题', '交付时间、交付方式或交付内容存在问题', 1, 0, 1, 50),
('renting', 'demander', 'other', '其他', '其他租赁中争议', 0, 0, 1, 60),
('wait_review', 'supplier', 'return_damage', '归还损坏', '归还时发现损坏', 1, 1, 1, 10),
('wait_review', 'supplier', 'missing_parts', '配件缺失', '配件、部件或随附物品缺失', 1, 1, 1, 20),
('wait_review', 'supplier', 'late_return', '逾期归还', '超过约定归还时间', 1, 0, 1, 30),
('wait_review', 'supplier', 'not_returned', '未完整归还', '部分道具或实例未归还', 1, 1, 1, 40),
('wait_review', 'supplier', 'cleaning_or_restore_issue', '清洁/复原问题', '清洁、复原、包装不符合归还要求', 1, 0, 1, 50),
('wait_review', 'supplier', 'other', '其他', '其他归还后争议', 0, 0, 1, 60);

INSERT INTO `admin_account` (`user_id`, `username`, `password_hash`, `enabled`)
SELECT 3, 'admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 1
WHERE EXISTS (SELECT 1 FROM `sys_user` WHERE `id` = 3)
ON DUPLICATE KEY UPDATE `enabled` = VALUES(`enabled`);
