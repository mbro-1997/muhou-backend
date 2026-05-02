CREATE TABLE IF NOT EXISTS `sys_user_wechat` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '微信绑定主键ID',
  `user_id` BIGINT NOT NULL COMMENT '关联用户ID',
  `openid` VARCHAR(64) NOT NULL COMMENT '微信OpenID',
  `unionid` VARCHAR(64) DEFAULT NULL COMMENT '微信UnionID',
  `session_key` VARCHAR(128) DEFAULT NULL COMMENT '微信会话密钥',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_sys_user_wechat_openid` (`openid`),
  UNIQUE KEY `uk_sys_user_wechat_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户微信绑定表';
