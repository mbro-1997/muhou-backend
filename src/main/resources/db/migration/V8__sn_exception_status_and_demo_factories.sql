SET @drop_prop_qr_prop_unique = (
  SELECT IF(
    COUNT(1) > 0,
    'ALTER TABLE `prop_qr_code` DROP INDEX `uk_prop_qr_prop_id`',
    'SELECT 1'
  )
  FROM information_schema.statistics
  WHERE table_schema = DATABASE()
    AND table_name = 'prop_qr_code'
    AND index_name = 'uk_prop_qr_prop_id'
);
PREPARE drop_prop_qr_prop_unique_stmt FROM @drop_prop_qr_prop_unique;
EXECUTE drop_prop_qr_prop_unique_stmt;
DEALLOCATE PREPARE drop_prop_qr_prop_unique_stmt;

SET @add_prop_qr_prop_idx = (
  SELECT IF(
    COUNT(1) = 0,
    'ALTER TABLE `prop_qr_code` ADD KEY `idx_prop_qr_prop_id` (`prop_id`)',
    'SELECT 1'
  )
  FROM information_schema.statistics
  WHERE table_schema = DATABASE()
    AND table_name = 'prop_qr_code'
    AND index_name = 'idx_prop_qr_prop_id'
);
PREPARE add_prop_qr_prop_idx_stmt FROM @add_prop_qr_prop_idx;
EXECUTE add_prop_qr_prop_idx_stmt;
DEALLOCATE PREPARE add_prop_qr_prop_idx_stmt;

ALTER TABLE `prop_instance`
  ADD COLUMN `status_remark` VARCHAR(255) DEFAULT NULL COMMENT 'SN status remark',
  ADD COLUMN `status_changed_at` DATETIME DEFAULT NULL COMMENT 'SN status changed time',
  ADD COLUMN `status_changed_by` BIGINT DEFAULT NULL COMMENT 'SN status operator user id';

CREATE TABLE `prop_instance_status_log` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `prop_instance_id` BIGINT NOT NULL COMMENT 'SN instance id',
  `prop_id` BIGINT NOT NULL COMMENT 'SKU id',
  `instance_no` VARCHAR(64) DEFAULT NULL COMMENT 'SN snapshot',
  `qr_code_id` VARCHAR(64) DEFAULT NULL COMMENT 'QR code snapshot',
  `from_status` VARCHAR(32) DEFAULT NULL COMMENT 'from status',
  `to_status` VARCHAR(32) NOT NULL COMMENT 'to status',
  `reason` VARCHAR(255) DEFAULT NULL COMMENT 'change reason',
  `operator_user_id` BIGINT NOT NULL COMMENT 'operator user id',
  `operator_role` VARCHAR(32) NOT NULL COMMENT 'supplier/admin/system',
  `related_order_id` BIGINT DEFAULT NULL COMMENT 'related order id',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_instance_status_log_instance` (`prop_instance_id`, `created_at`),
  KEY `idx_instance_status_log_prop` (`prop_id`, `created_at`),
  KEY `idx_instance_status_log_order` (`related_order_id`),
  KEY `idx_instance_status_log_operator` (`operator_user_id`, `created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='SN status change log';

INSERT INTO `sys_user` (
  `id`, `nickname`, `avatar_url`, `phone`, `realname_verified`, `student_verified`,
  `credit_score`, `user_status`, `register_status`, `created_at`, `updated_at`
) VALUES
  (4, 'factory_demo_a', '/images/avatar.png', '13800000004', 1, 0, 80, 'active', 'active', NOW(), NOW()),
  (5, 'factory_demo_b', '/images/avatar.png', '13800000005', 1, 0, 80, 'active', 'active', NOW(), NOW())
ON DUPLICATE KEY UPDATE
  `nickname` = VALUES(`nickname`),
  `phone` = VALUES(`phone`),
  `register_status` = 'active',
  `updated_at` = NOW();

INSERT INTO `sys_user_role` (`user_id`, `role_code`, `enabled`, `created_at`) VALUES
  (4, 'supplier', 1, NOW()),
  (5, 'supplier', 1, NOW())
ON DUPLICATE KEY UPDATE `enabled` = 1;

INSERT INTO `sys_user_wechat` (`user_id`, `openid`, `session_key`, `created_at`, `updated_at`) VALUES
  (4, 'demo-openid-factory_demo_a', 'demo-session-factory_demo_a', NOW(), NOW()),
  (5, 'demo-openid-factory_demo_b', 'demo-session-factory_demo_b', NOW(), NOW())
ON DUPLICATE KEY UPDATE
  `openid` = VALUES(`openid`),
  `session_key` = VALUES(`session_key`),
  `updated_at` = NOW();

INSERT INTO `factory_profile` (
  `owner_user_id`, `factory_name`, `unified_social_credit_code`, `business_license_url`,
  `contact_name`, `contact_phone`, `factory_address`, `main_business`, `status`,
  `approved_by_admin_user_id`, `approved_at`, `created_at`, `updated_at`
) VALUES
  (4, 'factory_demo_a', 'DEMOFACTORYA000001', '/images/stage-prop-real.jpg', 'Factory A', '13800000004', 'Shanghai Demo Factory A', 'stage props', 'active', 3, NOW(), NOW(), NOW()),
  (5, 'factory_demo_b', 'DEMOFACTORYB000001', '/images/stage-prop-real.jpg', 'Factory B', '13800000005', 'Shanghai Demo Factory B', 'stage props', 'active', 3, NOW(), NOW(), NOW())
ON DUPLICATE KEY UPDATE
  `factory_name` = VALUES(`factory_name`),
  `contact_phone` = VALUES(`contact_phone`),
  `factory_address` = VALUES(`factory_address`),
  `status` = 'active',
  `updated_at` = NOW();

INSERT INTO `prop_info` (
  `id`, `supplier_user_id`, `prop_name`, `image_url`, `style_code`, `type_code`,
  `size_desc`, `length_cm`, `width_cm`, `height_cm`, `material_desc`,
  `daily_rent_price_fen`, `deposit_amount_fen`, `fire_resistant_option`, `weight_desc`,
  `transport_suggestion`, `prop_status`, `audit_status`, `fill_status`, `remark`,
  `created_at`, `updated_at`
) VALUES
  (9001, 4, 'Factory A Demo Light', '/images/stage-prop-real.jpg', '现代', '特效设备', 'demo light', 80.00, 60.00, 120.00, 'metal', 5000, 20000, '是', '20kg', 'small van', 'idle', 'approved', 'filled', 'multi-factory split demo SKU A', NOW(), NOW()),
  (9002, 5, 'Factory B Demo Chair', '/images/stage-prop-real.jpg', '通用', '桌椅家具', 'demo chair', 50.00, 50.00, 90.00, 'wood', 3000, 10000, '否', '8kg', 'small van', 'idle', 'approved', 'filled', 'multi-factory split demo SKU B', NOW(), NOW())
ON DUPLICATE KEY UPDATE
  `supplier_user_id` = VALUES(`supplier_user_id`),
  `prop_name` = VALUES(`prop_name`),
  `prop_status` = 'idle',
  `audit_status` = 'approved',
  `fill_status` = 'filled',
  `updated_at` = NOW();

INSERT INTO `prop_image` (`prop_id`, `image_url`, `sort_order`, `created_at`)
SELECT 9001, '/images/stage-prop-real.jpg', 0, NOW()
WHERE NOT EXISTS (SELECT 1 FROM `prop_image` WHERE `prop_id` = 9001 AND `image_url` = '/images/stage-prop-real.jpg');

INSERT INTO `prop_image` (`prop_id`, `image_url`, `sort_order`, `created_at`)
SELECT 9002, '/images/stage-prop-real.jpg', 0, NOW()
WHERE NOT EXISTS (SELECT 1 FROM `prop_image` WHERE `prop_id` = 9002 AND `image_url` = '/images/stage-prop-real.jpg');

INSERT INTO `prop_qr_code` (
  `prop_id`, `qr_code_id`, `qr_scene`, `qr_page`, `qr_image_url`, `status`,
  `created_by_admin_user_id`, `used_by_supplier_user_id`, `used_at`, `created_at`, `updated_at`
) VALUES
  (9001, 'DEMO-A-SN-001', 'q=DEMO-A-SN-001', 'pages/qr-entry/index', '/images/stage-prop-real.jpg', 'filled', 3, 4, NOW(), NOW(), NOW()),
  (9001, 'DEMO-A-SN-002', 'q=DEMO-A-SN-002', 'pages/qr-entry/index', '/images/stage-prop-real.jpg', 'filled', 3, 4, NOW(), NOW(), NOW()),
  (9002, 'DEMO-B-SN-001', 'q=DEMO-B-SN-001', 'pages/qr-entry/index', '/images/stage-prop-real.jpg', 'filled', 3, 5, NOW(), NOW(), NOW()),
  (9002, 'DEMO-B-SN-002', 'q=DEMO-B-SN-002', 'pages/qr-entry/index', '/images/stage-prop-real.jpg', 'filled', 3, 5, NOW(), NOW(), NOW())
ON DUPLICATE KEY UPDATE
  `prop_id` = VALUES(`prop_id`),
  `status` = 'filled',
  `used_by_supplier_user_id` = VALUES(`used_by_supplier_user_id`),
  `updated_at` = NOW();

INSERT INTO `prop_instance` (
  `prop_id`, `qr_code_id`, `instance_no`, `instance_status`, `current_order_id`, `remark`,
  `status_remark`, `status_changed_at`, `status_changed_by`, `created_at`, `updated_at`
) VALUES
  (9001, 'DEMO-A-SN-001', 'A-SN-001', 'idle', NULL, 'demo instance', NULL, NOW(), 3, NOW(), NOW()),
  (9001, 'DEMO-A-SN-002', 'A-SN-002', 'idle', NULL, 'demo instance', NULL, NOW(), 3, NOW(), NOW()),
  (9002, 'DEMO-B-SN-001', 'B-SN-001', 'idle', NULL, 'demo instance', NULL, NOW(), 3, NOW(), NOW()),
  (9002, 'DEMO-B-SN-002', 'B-SN-002', 'idle', NULL, 'demo instance', NULL, NOW(), 3, NOW(), NOW())
ON DUPLICATE KEY UPDATE
  `prop_id` = VALUES(`prop_id`),
  `instance_no` = VALUES(`instance_no`),
  `instance_status` = 'idle',
  `current_order_id` = NULL,
  `updated_at` = NOW();
