ALTER TABLE `rental_order_item`
  CHANGE COLUMN `daily_rent_price_snapshot` `daily_rent_price_fen_snapshot` INT NOT NULL DEFAULT 0 COMMENT '下单时日租金快照，单位分',
  CHANGE COLUMN `deposit_amount_snapshot` `deposit_amount_fen_snapshot` INT NOT NULL DEFAULT 0 COMMENT '下单时押金快照，单位分';

ALTER TABLE `rental_order_item`
  DROP COLUMN `is_damaged`,
  DROP COLUMN `damage_remark`;
