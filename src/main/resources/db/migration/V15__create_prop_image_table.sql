CREATE TABLE IF NOT EXISTS `prop_image` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '道具图片主键ID',
  `prop_id` BIGINT NOT NULL COMMENT '关联道具ID',
  `image_url` VARCHAR(500) NOT NULL COMMENT '图片地址',
  `sort_order` INT NOT NULL DEFAULT 0 COMMENT '排序号，越小越靠前',
  `main_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '是否首图：0否 1是',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_prop_image_prop_sort` (`prop_id`, `sort_order`, `id`),
  KEY `idx_prop_image_prop_main` (`prop_id`, `main_flag`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='道具图片表';

INSERT INTO `prop_image` (`prop_id`, `image_url`, `sort_order`, `main_flag`, `created_at`)
SELECT p.`id`, TRIM(SUBSTRING_INDEX(p.`image_url`, ',', 1)), 0, 1, NOW()
FROM `prop_info` p
WHERE p.`image_url` IS NOT NULL
  AND TRIM(p.`image_url`) <> ''
  AND NOT EXISTS (
    SELECT 1 FROM `prop_image` pi WHERE pi.`prop_id` = p.`id`
  );

INSERT INTO `prop_image` (`prop_id`, `image_url`, `sort_order`, `main_flag`, `created_at`)
SELECT p.`id`, TRIM(SUBSTRING_INDEX(SUBSTRING_INDEX(p.`image_url`, ',', n.`sort_order` + 1), ',', -1)), n.`sort_order`, 0, NOW()
FROM `prop_info` p
JOIN (
  SELECT 1 AS `sort_order` UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
  UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7
) n
WHERE p.`image_url` IS NOT NULL
  AND TRIM(p.`image_url`) <> ''
  AND (LENGTH(p.`image_url`) - LENGTH(REPLACE(p.`image_url`, ',', ''))) >= n.`sort_order`
  AND TRIM(SUBSTRING_INDEX(SUBSTRING_INDEX(p.`image_url`, ',', n.`sort_order` + 1), ',', -1)) <> ''
  AND NOT EXISTS (
    SELECT 1
    FROM `prop_image` pi
    WHERE pi.`prop_id` = p.`id`
      AND pi.`sort_order` = n.`sort_order`
  );

UPDATE `prop_info`
SET `image_url` = TRIM(SUBSTRING_INDEX(`image_url`, ',', 1))
WHERE `image_url` IS NOT NULL
  AND `image_url` LIKE '%,%';
