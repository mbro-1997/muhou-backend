CREATE TABLE IF NOT EXISTS `project_scheme` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '方案主键ID',
  `user_id` BIGINT NOT NULL COMMENT '所属需求方用户ID',
  `project_name` VARCHAR(128) NOT NULL COMMENT '方案名称',
  `project_description` VARCHAR(500) DEFAULT NULL COMMENT '方案说明',
  `project_status` VARCHAR(20) NOT NULL DEFAULT 'editing' COMMENT '方案状态：editing编辑中 ordered已下单',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `ordered_at` DATETIME DEFAULT NULL COMMENT '转订单时间',
  PRIMARY KEY (`id`),
  KEY `idx_project_user_status` (`user_id`, `project_status`, `updated_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='方案主表';

CREATE TABLE IF NOT EXISTS `project_scheme_item` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '方案道具明细主键ID',
  `project_id` BIGINT NOT NULL COMMENT '关联方案ID',
  `prop_id` BIGINT NOT NULL COMMENT '关联道具ID',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '加入方案时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_project_prop` (`project_id`, `prop_id`),
  KEY `idx_project_item_prop` (`prop_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='方案道具明细表';

CREATE TABLE IF NOT EXISTS `rental_order_item` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '订单道具明细主键ID',
  `order_id` BIGINT NOT NULL COMMENT '关联订单ID',
  `prop_id` BIGINT NOT NULL COMMENT '关联道具ID',
  `prop_name_snapshot` VARCHAR(128) NOT NULL COMMENT '道具名称快照',
  `image_url_snapshot` VARCHAR(255) DEFAULT NULL COMMENT '道具图片快照',
  `daily_rent_price_fen_snapshot` INT NOT NULL DEFAULT 0 COMMENT '下单时日租金快照，单位分',
  `deposit_amount_fen_snapshot` INT NOT NULL DEFAULT 0 COMMENT '下单时押金快照，单位分',
  `outbound_status` VARCHAR(20) NOT NULL DEFAULT 'pending' COMMENT '出库状态：pending待出库 scanned已出库',
  `return_status` VARCHAR(20) NOT NULL DEFAULT 'pending' COMMENT '归还状态：pending待归还 scanned已归还',
  `outbound_scanned_at` DATETIME DEFAULT NULL COMMENT '出库扫码时间',
  `return_scanned_at` DATETIME DEFAULT NULL COMMENT '归还扫码时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_prop` (`order_id`, `prop_id`),
  KEY `idx_order_item_prop` (`prop_id`, `return_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单道具明细表';

CREATE TABLE IF NOT EXISTS `order_review` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '订单评价主键ID',
  `order_id` BIGINT NOT NULL COMMENT '关联订单ID',
  `reviewer_user_id` BIGINT NOT NULL COMMENT '评价人用户ID',
  `reviewer_role` VARCHAR(20) NOT NULL COMMENT '评价角色：demander/supplier',
  `score` INT NOT NULL COMMENT '评分，建议1-5',
  `content` VARCHAR(1000) DEFAULT NULL COMMENT '评价内容',
  `visible_flag` TINYINT NOT NULL DEFAULT 1 COMMENT '是否前台展示：0否 1是',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '评价时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_reviewer_role` (`order_id`, `reviewer_role`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单评价表';
