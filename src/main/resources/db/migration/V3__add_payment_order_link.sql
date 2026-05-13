ALTER TABLE `order_payment`
  MODIFY COLUMN `order_id` BIGINT NULL COMMENT '兼容旧单订单支付；批量支付时为空或为首个订单ID';

CREATE TABLE `payment_order_link` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
  `payment_id` BIGINT NOT NULL COMMENT '支付流水ID',
  `order_id` BIGINT NOT NULL COMMENT '订单ID',
  `order_no` VARCHAR(64) NOT NULL COMMENT '订单号快照',
  `supplier_user_id` BIGINT NOT NULL COMMENT '商家用户ID快照',
  `rent_amount_fen` INT NOT NULL DEFAULT 0 COMMENT '该订单租金金额，单位分',
  `deposit_amount_fen` INT NOT NULL DEFAULT 0 COMMENT '该订单押金金额，单位分',
  `total_amount_fen` INT NOT NULL DEFAULT 0 COMMENT '该订单支付分摊总额，单位分',
  `refund_amount_fen` INT NOT NULL DEFAULT 0 COMMENT '该订单已退款金额，单位分',
  `settlement_amount_fen` INT NOT NULL DEFAULT 0 COMMENT '该订单已结算给商家的金额，单位分',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_payment_order_link_payment_order` (`payment_id`, `order_id`),
  KEY `idx_payment_order_link_payment` (`payment_id`),
  KEY `idx_payment_order_link_order` (`order_id`),
  KEY `idx_payment_order_link_supplier` (`supplier_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='支付订单关联表';
