INSERT INTO `sys_user` (`id`, `nickname`, `avatar_url`, `phone`, `realname_verified`, `student_verified`, `user_status`, `created_at`, `updated_at`)
VALUES
  (1, '租赁端演示账号', '/images/avatar.png', '13800000001', 1, 1, 'active', NOW(), NOW()),
  (2, '工厂端演示账号', '/images/avatar.png', '13800000002', 1, 1, 'active', NOW(), NOW()),
  (3, '管理员演示账号', '/images/avatar.png', '13800000003', 1, 1, 'active', NOW(), NOW())
ON DUPLICATE KEY UPDATE
  `nickname` = VALUES(`nickname`),
  `avatar_url` = VALUES(`avatar_url`),
  `phone` = VALUES(`phone`),
  `realname_verified` = VALUES(`realname_verified`),
  `student_verified` = VALUES(`student_verified`),
  `user_status` = VALUES(`user_status`),
  `updated_at` = NOW();

INSERT INTO `sys_user_role` (`user_id`, `role_code`, `enabled`, `created_at`)
VALUES
  (1, 'demander', 1, NOW()),
  (2, 'supplier', 1, NOW()),
  (3, 'admin', 1, NOW()),
  (3, 'supplier', 1, NOW())
ON DUPLICATE KEY UPDATE
  `enabled` = VALUES(`enabled`);

UPDATE `sys_user_role`
SET `enabled` = 0
WHERE (`user_id` = 1 AND `role_code` IN ('supplier', 'admin'))
   OR (`user_id` = 3 AND `role_code` = 'demander');
