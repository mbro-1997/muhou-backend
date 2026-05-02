ALTER TABLE `order_review`
  ADD COLUMN `prop_score` INT NULL COMMENT '道具本身评分：1-5' AFTER `score`,
  ADD COLUMN `counterparty_score` INT NULL COMMENT '对方评分：1-5' AFTER `prop_score`,
  ADD COLUMN `auto_flag` TINYINT NOT NULL DEFAULT 0 COMMENT '是否系统默认好评：0否 1是' AFTER `visible_flag`;

UPDATE `order_review`
SET `prop_score` = IFNULL(`prop_score`, `score`),
    `counterparty_score` = IFNULL(`counterparty_score`, `score`)
WHERE `prop_score` IS NULL OR `counterparty_score` IS NULL;
