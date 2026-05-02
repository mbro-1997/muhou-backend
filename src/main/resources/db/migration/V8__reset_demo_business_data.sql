DELETE FROM `order_review`
WHERE `order_id` IN (
  SELECT `id` FROM (
    SELECT `id`
    FROM `rental_order`
    WHERE `demander_user_id` IN (1, 2, 3)
       OR `supplier_user_id` IN (1, 2, 3)
  ) AS tmp
);

DELETE FROM `order_dispute`
WHERE `order_id` IN (
  SELECT `id` FROM (
    SELECT `id`
    FROM `rental_order`
    WHERE `demander_user_id` IN (1, 2, 3)
       OR `supplier_user_id` IN (1, 2, 3)
  ) AS tmp
);

DELETE FROM `payment_notify_log`
WHERE `payment_no` IN (
  SELECT `payment_no` FROM (
    SELECT `payment_no`
    FROM `order_payment`
    WHERE `order_id` IN (
      SELECT `id` FROM (
        SELECT `id`
        FROM `rental_order`
        WHERE `demander_user_id` IN (1, 2, 3)
           OR `supplier_user_id` IN (1, 2, 3)
      ) AS tmp_order
    )
  ) AS tmp_payment
)
   OR `merchant_out_trade_no` IN (
  SELECT `merchant_out_trade_no` FROM (
    SELECT `merchant_out_trade_no`
    FROM `order_payment`
    WHERE `order_id` IN (
      SELECT `id` FROM (
        SELECT `id`
        FROM `rental_order`
        WHERE `demander_user_id` IN (1, 2, 3)
           OR `supplier_user_id` IN (1, 2, 3)
      ) AS tmp_order
    )
  ) AS tmp_trade
);

DELETE FROM `order_refund`
WHERE `order_id` IN (
  SELECT `id` FROM (
    SELECT `id`
    FROM `rental_order`
    WHERE `demander_user_id` IN (1, 2, 3)
       OR `supplier_user_id` IN (1, 2, 3)
  ) AS tmp
);

DELETE FROM `order_payment`
WHERE `order_id` IN (
  SELECT `id` FROM (
    SELECT `id`
    FROM `rental_order`
    WHERE `demander_user_id` IN (1, 2, 3)
       OR `supplier_user_id` IN (1, 2, 3)
  ) AS tmp
);

DELETE FROM `rental_order_item`
WHERE `order_id` IN (
  SELECT `id` FROM (
    SELECT `id`
    FROM `rental_order`
    WHERE `demander_user_id` IN (1, 2, 3)
       OR `supplier_user_id` IN (1, 2, 3)
  ) AS tmp
);

DELETE FROM `rental_order`
WHERE `demander_user_id` IN (1, 2, 3)
   OR `supplier_user_id` IN (1, 2, 3);

DELETE FROM `project_scheme_item`
WHERE `project_id` IN (
  SELECT `id` FROM (
    SELECT `id`
    FROM `project_scheme`
    WHERE `user_id` IN (1, 2, 3)
  ) AS tmp
);

DELETE FROM `project_scheme`
WHERE `user_id` IN (1, 2, 3);

DELETE FROM `prop_audit`
WHERE `prop_id` IN (
  SELECT `id` FROM (
    SELECT `id`
    FROM `prop_info`
    WHERE `supplier_user_id` IN (2, 3)
  ) AS tmp
);

DELETE FROM `prop_info`
WHERE `supplier_user_id` IN (2, 3);

INSERT INTO `prop_info` (
  `id`, `supplier_user_id`, `prop_name`, `image_url`, `style_code`, `type_code`, `size_desc`,
  `length_cm`, `width_cm`, `height_cm`, `material_desc`, `daily_rent_price_fen`, `deposit_amount_fen`,
  `fire_rating`, `transport_requirement`, `transport_suggestion`, `prop_status`, `audit_status`,
  `qr_code_id`, `qr_code_url`, `fill_status`, `remark`, `created_at`, `updated_at`
) VALUES
  (101, 2, '舞台追光灯 A01', '/images/stage-prop-real.jpg', '现代', '灯光', '120cm x 80cm x 70cm', 120.00, 80.00, 70.00, '金属', 12000, 40000, 'B1', '防震运输', '厢式货车', 'idle', 'approved', 'QR-DEMO-0101', NULL, 'filled', '工厂演示道具', NOW(), NOW()),
  (102, 2, '舞台追光灯 A02', '/images/stage-prop-real.jpg', '现代', '灯光', '120cm x 80cm x 70cm', 120.00, 80.00, 70.00, '金属', 12000, 40000, 'B1', '防震运输', '厢式货车', 'idle', 'approved', 'QR-DEMO-0102', NULL, 'filled', '工厂演示道具', NOW(), NOW()),
  (103, 2, '舞台染色灯 B01', '/images/stage-prop-real.jpg', '现代', '灯光', '90cm x 60cm x 60cm', 90.00, 60.00, 60.00, '金属', 9000, 30000, 'B1', '防震运输', '厢式货车', 'idle', 'approved', 'QR-DEMO-0103', NULL, 'filled', '工厂演示道具', NOW(), NOW()),
  (104, 2, '舞台染色灯 B02', '/images/stage-prop-real.jpg', '现代', '灯光', '90cm x 60cm x 60cm', 90.00, 60.00, 60.00, '金属', 9000, 30000, 'B1', '防震运输', '厢式货车', 'idle', 'approved', 'QR-DEMO-0104', NULL, 'filled', '工厂演示道具', NOW(), NOW()),
  (105, 2, '专业音箱 C01', '/images/stage-prop-real.jpg', '现代', '音响', '85cm x 55cm x 50cm', 85.00, 55.00, 50.00, '工程塑料+金属', 15000, 50000, 'B1', '立放固定', '中型货车', 'idle', 'approved', 'QR-DEMO-0105', NULL, 'filled', '工厂演示道具', NOW(), NOW()),
  (106, 2, '专业音箱 C02', '/images/stage-prop-real.jpg', '现代', '音响', '85cm x 55cm x 50cm', 85.00, 55.00, 50.00, '工程塑料+金属', 15000, 50000, 'B1', '立放固定', '中型货车', 'idle', 'approved', 'QR-DEMO-0106', NULL, 'filled', '工厂演示道具', NOW(), NOW()),
  (107, 2, '无线麦克风套装 D01', '/images/stage-prop-real.jpg', '现代', '音响', '手持套装', 25.00, 18.00, 10.00, '工程塑料', 6000, 20000, 'B1', '箱体运输', '小型货车', 'idle', 'approved', 'QR-DEMO-0107', NULL, 'filled', '工厂演示道具', NOW(), NOW()),
  (108, 2, '无线麦克风套装 D02', '/images/stage-prop-real.jpg', '现代', '音响', '手持套装', 25.00, 18.00, 10.00, '工程塑料', 6000, 20000, 'B1', '箱体运输', '小型货车', 'idle', 'approved', 'QR-DEMO-0108', NULL, 'filled', '工厂演示道具', NOW(), NOW()),
  (109, 2, 'LED 屏幕模组 E01', '/images/stage-prop-real.jpg', '科技', '屏幕', '200cm x 120cm x 18cm', 200.00, 120.00, 18.00, 'LED+金属', 20000, 80000, 'B1', '木箱运输', '厢式货车', 'idle', 'approved', 'QR-DEMO-0109', NULL, 'filled', '工厂演示道具', NOW(), NOW()),
  (110, 2, 'LED 屏幕模组 E02', '/images/stage-prop-real.jpg', '科技', '屏幕', '200cm x 120cm x 18cm', 200.00, 120.00, 18.00, 'LED+金属', 20000, 80000, 'B1', '木箱运输', '厢式货车', 'idle', 'approved', 'QR-DEMO-0110', NULL, 'filled', '工厂演示道具', NOW(), NOW()),
  (111, 2, '舞台桁架 F01', '/images/stage-prop-real.jpg', '工业', '结构', '300cm x 40cm x 40cm', 300.00, 40.00, 40.00, '铝合金', 10000, 35000, 'A', '捆扎运输', '平板车', 'idle', 'approved', 'QR-DEMO-0111', NULL, 'filled', '工厂演示道具', NOW(), NOW()),
  (112, 2, '舞台桁架 F02', '/images/stage-prop-real.jpg', '工业', '结构', '300cm x 40cm x 40cm', 300.00, 40.00, 40.00, '铝合金', 10000, 35000, 'A', '捆扎运输', '平板车', 'idle', 'approved', 'QR-DEMO-0112', NULL, 'filled', '工厂演示道具', NOW(), NOW()),
  (113, 2, '背景幕布 G01', '/images/stage-prop-real.jpg', '传统', '布景', '500cm x 300cm x 8cm', 500.00, 300.00, 8.00, '阻燃布', 8000, 25000, 'B1', '卷装运输', '小型货车', 'idle', 'approved', 'QR-DEMO-0113', NULL, 'filled', '工厂演示道具', NOW(), NOW()),
  (114, 2, '背景幕布 G02', '/images/stage-prop-real.jpg', '传统', '布景', '500cm x 300cm x 8cm', 500.00, 300.00, 8.00, '阻燃布', 8000, 25000, 'B1', '卷装运输', '小型货车', 'idle', 'approved', 'QR-DEMO-0114', NULL, 'filled', '工厂演示道具', NOW(), NOW()),
  (115, 2, '舞台升降台 H01', '/images/stage-prop-real.jpg', '现代', '结构', '220cm x 140cm x 60cm', 220.00, 140.00, 60.00, '钢制结构', 18000, 70000, 'A', '平稳装卸', '平板车', 'idle', 'approved', 'QR-DEMO-0115', NULL, 'filled', '工厂演示道具', NOW(), NOW()),
  (116, 2, '舞台升降台 H02', '/images/stage-prop-real.jpg', '现代', '结构', '220cm x 140cm x 60cm', 220.00, 140.00, 60.00, '钢制结构', 18000, 70000, 'A', '平稳装卸', '平板车', 'idle', 'approved', 'QR-DEMO-0116', NULL, 'filled', '工厂演示道具', NOW(), NOW()),
  (117, 2, '主持台 I01', '/images/stage-prop-real.jpg', '现代', '道具', '110cm x 60cm x 110cm', 110.00, 60.00, 110.00, '木质+金属', 5000, 15000, 'B1', '防磕碰运输', '小型货车', 'idle', 'approved', 'QR-DEMO-0117', NULL, 'filled', '工厂演示道具', NOW(), NOW()),
  (118, 2, '主持台 I02', '/images/stage-prop-real.jpg', '现代', '道具', '110cm x 60cm x 110cm', 110.00, 60.00, 110.00, '木质+金属', 5000, 15000, 'B1', '防磕碰运输', '小型货车', 'idle', 'approved', 'QR-DEMO-0118', NULL, 'filled', '工厂演示道具', NOW(), NOW()),
  (119, 2, '舞台效果机 J01', '/images/stage-prop-real.jpg', '科技', '特效', '70cm x 45cm x 50cm', 70.00, 45.00, 50.00, '金属', 7000, 22000, 'B1', '防倾倒运输', '小型货车', 'idle', 'approved', 'QR-DEMO-0119', NULL, 'filled', '工厂演示道具', NOW(), NOW()),
  (120, 2, '舞台效果机 J02', '/images/stage-prop-real.jpg', '科技', '特效', '70cm x 45cm x 50cm', 70.00, 45.00, 50.00, '金属', 7000, 22000, 'B1', '防倾倒运输', '小型货车', 'idle', 'approved', 'QR-DEMO-0120', NULL, 'filled', '工厂演示道具', NOW(), NOW())
ON DUPLICATE KEY UPDATE
  `supplier_user_id` = VALUES(`supplier_user_id`),
  `prop_name` = VALUES(`prop_name`),
  `image_url` = VALUES(`image_url`),
  `style_code` = VALUES(`style_code`),
  `type_code` = VALUES(`type_code`),
  `size_desc` = VALUES(`size_desc`),
  `length_cm` = VALUES(`length_cm`),
  `width_cm` = VALUES(`width_cm`),
  `height_cm` = VALUES(`height_cm`),
  `material_desc` = VALUES(`material_desc`),
  `daily_rent_price_fen` = VALUES(`daily_rent_price_fen`),
  `deposit_amount_fen` = VALUES(`deposit_amount_fen`),
  `fire_rating` = VALUES(`fire_rating`),
  `transport_requirement` = VALUES(`transport_requirement`),
  `transport_suggestion` = VALUES(`transport_suggestion`),
  `prop_status` = VALUES(`prop_status`),
  `audit_status` = VALUES(`audit_status`),
  `qr_code_id` = VALUES(`qr_code_id`),
  `qr_code_url` = VALUES(`qr_code_url`),
  `fill_status` = VALUES(`fill_status`),
  `remark` = VALUES(`remark`),
  `updated_at` = NOW();
