INSERT INTO `prop_info` (
  `id`, `supplier_user_id`, `prop_name`, `image_url`, `style_code`, `type_code`, `size_desc`,
  `length_cm`, `width_cm`, `height_cm`, `material_desc`, `daily_rent_price_fen`, `deposit_amount_fen`,
  `fire_rating`, `transport_requirement`, `transport_suggestion`, `prop_status`, `audit_status`,
  `qr_code_id`, `qr_code_url`, `fill_status`, `remark`
) VALUES
  (1, 2, '舞台聚光灯', '/images/default-goods-image.png', '现代', '灯光', '120cm x 80cm', 120.00, 80.00, NULL, '金属', 10000, 50000, 'B1', '防震运输', '厢式货车', 'idle', 'approved', 'QR-PROP-0001', NULL, 'filled', '演示数据'),
  (2, 2, '专业音响系统', '/images/default-goods-image.png', '现代', '音响', '80cm x 60cm', 80.00, 60.00, NULL, '塑料+金属', 15000, 80000, 'B1', '立放固定', '中型货车', 'locked', 'approved', 'QR-PROP-0002', NULL, 'filled', '演示数据'),
  (3, 2, 'LED 显示屏', '/images/default-goods-image.png', '科技', '屏幕', '200cm x 120cm', 200.00, 120.00, NULL, 'LED+金属', 20000, 100000, 'B1', '防震木箱', '厢式货车', 'renting', 'approved', 'QR-PROP-0003', NULL, 'filled', '演示数据'),
  (4, 2, '舞台桁架', '/images/default-goods-image.png', '工业', '结构', '300cm x 200cm', 300.00, 200.00, NULL, '铝合金', 8000, 40000, 'A', '捆扎运输', '平板车', 'renting', 'approved', 'QR-PROP-0004', NULL, 'filled', '演示数据'),
  (5, 2, '背景幕布', '/images/default-goods-image.png', '传统', '布景', '500cm x 300cm', 500.00, 300.00, NULL, '阻燃布', 6000, 30000, 'B1', '卷装运输', '小型货车', 'offline', 'approved', 'QR-PROP-0005', NULL, 'filled', '演示数据'),
  (6, 2, '无线麦克风套装', '/images/default-goods-image.png', '现代', '音响', '手持', NULL, NULL, NULL, '工程塑料', 5000, 20000, 'B1', '防摔箱', '小型货车', 'idle', 'approved', 'QR-PROP-0006', NULL, 'filled', '演示数据')
ON DUPLICATE KEY UPDATE `id` = `id`;

INSERT INTO `project_scheme` (
  `id`, `user_id`, `project_name`, `project_description`, `project_status`, `created_at`, `updated_at`, `ordered_at`
) VALUES
  (1, 1, '毕业晚会方案', '校园舞台项目', 'editing', NOW(), NOW(), NULL)
ON DUPLICATE KEY UPDATE `id` = `id`;

INSERT IGNORE INTO `project_scheme_item` (`project_id`, `prop_id`, `created_at`) VALUES
  (1, 1, NOW()),
  (1, 2, NOW()),
  (1, 3, NOW());

INSERT INTO `rental_order` (
  `id`, `order_no`, `project_id`, `demander_user_id`, `supplier_user_id`, `order_status`,
  `rent_amount_fen`, `deposit_amount_fen`, `total_amount_fen`, `pay_status`, `refund_status`,
  `current_payment_id`, `total_paid_fen`, `total_refunded_fen`, `confirm_deadline_at`, `created_at`,
  `paid_at`, `confirmed_at`, `picked_up_at`, `returned_at`, `reviewed_at`, `cancelled_at`,
  `cancel_reason`, `deposit_refunded_flag`, `remark`, `created_by`, `updated_at`
) VALUES
  (1001, 'ORD-20260413-001', 1, 1, 2, 'pending_factory_confirm', 15000, 80000, 95000, 'paid', 'none', NULL, 95000, 0, DATE_ADD(NOW(), INTERVAL 40 MINUTE), DATE_SUB(NOW(), INTERVAL 20 MINUTE), DATE_SUB(NOW(), INTERVAL 19 MINUTE), NULL, NULL, NULL, NULL, NULL, NULL, 0, '演示待确认订单', 1, NOW()),
  (1002, 'ORD-20260412-002', 1, 1, 2, 'renting', 28000, 140000, 168000, 'paid', 'none', NULL, 168000, 0, DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 47 HOUR), DATE_SUB(NOW(), INTERVAL 46 HOUR), NULL, NULL, NULL, NULL, 0, '演示租赁中订单', 1, NOW())
ON DUPLICATE KEY UPDATE `id` = `id`;

INSERT IGNORE INTO `rental_order_item` (
  `order_id`, `prop_id`, `prop_name_snapshot`, `image_url_snapshot`,
  `daily_rent_price_snapshot`, `deposit_amount_snapshot`,
  `outbound_status`, `return_status`, `outbound_scanned_at`, `return_scanned_at`
) VALUES
  (1001, 2, '专业音响系统', '/images/default-goods-image.png', 15000, 80000, 'pending', 'pending', NULL, NULL),
  (1002, 3, 'LED 显示屏', '/images/default-goods-image.png', 20000, 100000, 'scanned', 'pending', DATE_SUB(NOW(), INTERVAL 46 HOUR), NULL),
  (1002, 4, '舞台桁架', '/images/default-goods-image.png', 8000, 40000, 'scanned', 'pending', DATE_SUB(NOW(), INTERVAL 46 HOUR), NULL);
