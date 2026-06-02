CREATE TABLE `prop_instance` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `prop_id` BIGINT NOT NULL COMMENT 'SKU id, references prop_info.id',
  `qr_code_id` VARCHAR(64) NOT NULL COMMENT 'unique QR code id',
  `instance_no` VARCHAR(64) DEFAULT NULL COMMENT 'physical asset number',
  `instance_status` VARCHAR(32) NOT NULL DEFAULT 'idle' COMMENT 'idle locked renting',
  `current_order_id` BIGINT DEFAULT NULL COMMENT 'current rental_order id',
  `remark` VARCHAR(255) DEFAULT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_prop_instance_qr_code_id` (`qr_code_id`),
  KEY `idx_prop_instance_prop_status` (`prop_id`, `instance_status`),
  KEY `idx_prop_instance_order` (`current_order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='prop physical instance';

CREATE TABLE `order_item_instance` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `order_id` BIGINT NOT NULL,
  `order_item_id` BIGINT NOT NULL,
  `prop_id` BIGINT NOT NULL,
  `prop_instance_id` BIGINT NOT NULL,
  `outbound_status` VARCHAR(20) NOT NULL DEFAULT 'pending',
  `return_status` VARCHAR(20) NOT NULL DEFAULT 'pending',
  `outbound_scanned_at` DATETIME DEFAULT NULL,
  `return_scanned_at` DATETIME DEFAULT NULL,
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_item_instance` (`order_id`, `prop_instance_id`),
  KEY `idx_order_item_instance_order_item` (`order_item_id`),
  KEY `idx_order_item_instance_prop` (`prop_id`),
  KEY `idx_order_item_instance_status` (`order_id`, `outbound_status`, `return_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='order bound physical instances';

ALTER TABLE `project_scheme_item`
  ADD COLUMN `quantity` INT NOT NULL DEFAULT 1 AFTER `prop_id`;

ALTER TABLE `rental_order_item`
  ADD COLUMN `quantity` INT NOT NULL DEFAULT 1 AFTER `prop_id`;

ALTER TABLE `prop_qr_code`
  DROP INDEX `uk_prop_qr_prop_id`;

ALTER TABLE `prop_qr_code`
  MODIFY COLUMN `prop_id` BIGINT NULL COMMENT 'legacy SKU/shell id';

ALTER TABLE `prop_qr_code`
  ADD KEY `idx_prop_qr_prop_id` (`prop_id`);

INSERT INTO `prop_instance` (
  `prop_id`,
  `qr_code_id`,
  `instance_no`,
  `instance_status`,
  `current_order_id`,
  `remark`,
  `created_at`,
  `updated_at`
)
SELECT
  p.`id`,
  p.`qr_code_id`,
  CONCAT('SN-', p.`id`),
  CASE
    WHEN p.`prop_status` = 'locked' THEN 'locked'
    WHEN p.`prop_status` = 'renting' THEN 'renting'
    ELSE 'idle'
  END,
  CASE
    WHEN p.`prop_status` IN ('locked', 'renting') THEN (
      SELECT ro.`id`
      FROM `rental_order_item` roi
      JOIN `rental_order` ro ON ro.`id` = roi.`order_id`
      WHERE roi.`prop_id` = p.`id`
        AND ro.`order_status` IN ('pending_factory_confirm', 'wait_pickup', 'renting')
      ORDER BY ro.`id` DESC
      LIMIT 1
    )
    ELSE NULL
  END,
  'migrated from legacy prop_info row',
  p.`created_at`,
  p.`updated_at`
FROM `prop_info` p
WHERE p.`fill_status` = 'filled'
  AND p.`qr_code_id` IS NOT NULL
  AND p.`qr_code_id` <> ''
  AND NOT EXISTS (
    SELECT 1 FROM `prop_instance` pi WHERE pi.`qr_code_id` = p.`qr_code_id`
  );

INSERT INTO `order_item_instance` (
  `order_id`,
  `order_item_id`,
  `prop_id`,
  `prop_instance_id`,
  `outbound_status`,
  `return_status`,
  `outbound_scanned_at`,
  `return_scanned_at`,
  `created_at`,
  `updated_at`
)
SELECT
  roi.`order_id`,
  roi.`id`,
  roi.`prop_id`,
  pi.`id`,
  roi.`outbound_status`,
  roi.`return_status`,
  roi.`outbound_scanned_at`,
  roi.`return_scanned_at`,
  NOW(),
  NOW()
FROM `rental_order_item` roi
JOIN `prop_instance` pi ON pi.`prop_id` = roi.`prop_id`
WHERE NOT EXISTS (
  SELECT 1
  FROM `order_item_instance` oii
  WHERE oii.`order_id` = roi.`order_id`
    AND oii.`prop_instance_id` = pi.`id`
);

UPDATE `prop_info`
SET `prop_status` = 'idle'
WHERE `prop_status` IN ('locked', 'renting');
