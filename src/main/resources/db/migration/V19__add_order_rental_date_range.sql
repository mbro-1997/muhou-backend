ALTER TABLE `rental_order`
  ADD COLUMN `rental_start_date` DATE NULL COMMENT '租赁开始日期' AFTER `rental_days`,
  ADD COLUMN `rental_end_date` DATE NULL COMMENT '租赁结束日期' AFTER `rental_start_date`;

UPDATE `rental_order`
SET `rental_start_date` = DATE(`created_at`),
    `rental_end_date` = DATE_ADD(DATE(`created_at`), INTERVAL GREATEST(IFNULL(`rental_days`, 1) - 1, 0) DAY)
WHERE `rental_start_date` IS NULL
   OR `rental_end_date` IS NULL;

CREATE INDEX `idx_rental_order_rental_date`
  ON `rental_order` (`rental_start_date`, `rental_end_date`);
