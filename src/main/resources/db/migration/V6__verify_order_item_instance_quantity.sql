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
  CASE
    WHEN pi.`instance_status` = 'renting' THEN 'scanned'
    ELSE 'pending'
  END,
  'pending',
  CASE
    WHEN pi.`instance_status` = 'renting' THEN NOW()
    ELSE NULL
  END,
  NULL,
  NOW(),
  NOW()
FROM `rental_order_item` roi
JOIN `prop_instance` pi
  ON pi.`prop_id` = roi.`prop_id`
 AND pi.`current_order_id` = roi.`order_id`
LEFT JOIN `order_item_instance` existing
  ON existing.`order_id` = roi.`order_id`
 AND existing.`prop_instance_id` = pi.`id`
WHERE existing.`id` IS NULL;

CREATE TEMPORARY TABLE `tmp_order_item_instance_overflow` (
  `id` BIGINT NOT NULL PRIMARY KEY
) ENGINE=MEMORY;

INSERT INTO `tmp_order_item_instance_overflow` (`id`)
SELECT ranked.`id`
FROM (
  SELECT
    sorted.`id`,
    sorted.`order_item_id`,
    sorted.`quantity`,
    @row_num := IF(@current_order_item_id = sorted.`order_item_id`, @row_num + 1, 1) AS `row_num`,
    @current_order_item_id := sorted.`order_item_id` AS `group_marker`
  FROM (
    SELECT
      oii.`id`,
      oii.`order_item_id`,
      GREATEST(IFNULL(roi.`quantity`, 1), 1) AS `quantity`
    FROM `order_item_instance` oii
    JOIN `rental_order_item` roi ON roi.`id` = oii.`order_item_id`
    ORDER BY oii.`order_item_id`, oii.`id`
  ) sorted
  CROSS JOIN (SELECT @row_num := 0, @current_order_item_id := 0) vars
) ranked
WHERE ranked.`row_num` > ranked.`quantity`;

DELETE oii
FROM `order_item_instance` oii
JOIN `tmp_order_item_instance_overflow` overflow ON overflow.`id` = oii.`id`;

DROP TEMPORARY TABLE `tmp_order_item_instance_overflow`;

CREATE OR REPLACE VIEW `v_order_item_instance_quantity_mismatch` AS
SELECT
  roi.`order_id`,
  roi.`id` AS `order_item_id`,
  roi.`prop_id`,
  GREATEST(IFNULL(roi.`quantity`, 1), 1) AS `expected_quantity`,
  COUNT(oii.`id`) AS `actual_instance_count`
FROM `rental_order_item` roi
LEFT JOIN `order_item_instance` oii ON oii.`order_item_id` = roi.`id`
GROUP BY
  roi.`order_id`,
  roi.`id`,
  roi.`prop_id`,
  roi.`quantity`
HAVING COUNT(oii.`id`) <> GREATEST(IFNULL(roi.`quantity`, 1), 1);
