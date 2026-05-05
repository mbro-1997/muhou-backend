CREATE TABLE IF NOT EXISTS prop_qr_code (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    prop_id BIGINT NOT NULL COMMENT '关联道具ID',
    qr_code_id VARCHAR(64) NOT NULL COMMENT '二维码业务标识',
    qr_scene VARCHAR(128) NOT NULL COMMENT '小程序码scene参数',
    qr_page VARCHAR(128) NOT NULL COMMENT '小程序码打开页面',
    qr_image_url VARCHAR(255) DEFAULT NULL COMMENT '二维码图片访问地址',
    qr_image_storage_key VARCHAR(255) DEFAULT NULL COMMENT '二维码图片存储key',
    image_sha256 VARCHAR(64) DEFAULT NULL COMMENT '二维码图片SHA256摘要',
    status VARCHAR(20) NOT NULL DEFAULT 'unused' COMMENT '二维码状态：unused未登记 filled已登记 revoked已作废',
    printed_flag TINYINT NOT NULL DEFAULT 0 COMMENT '是否已打印：0否 1是',
    printed_at DATETIME DEFAULT NULL COMMENT '打印时间',
    downloaded_at DATETIME DEFAULT NULL COMMENT '最近下载/查看时间',
    created_by_admin_user_id BIGINT DEFAULT NULL COMMENT '生成二维码的管理员用户ID',
    used_by_supplier_user_id BIGINT DEFAULT NULL COMMENT '扫码登记的工厂用户ID',
    used_at DATETIME DEFAULT NULL COMMENT '登记完成时间',
    revoked_by_admin_user_id BIGINT DEFAULT NULL COMMENT '作废管理员用户ID',
    revoked_at DATETIME DEFAULT NULL COMMENT '作废时间',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_prop_qr_code_id (qr_code_id),
    UNIQUE KEY uk_prop_qr_prop_id (prop_id),
    KEY idx_prop_qr_status (status),
    KEY idx_prop_qr_created_admin (created_by_admin_user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='道具二维码生命周期表';

INSERT INTO prop_qr_code (
    prop_id,
    qr_code_id,
    qr_scene,
    qr_page,
    qr_image_url,
    status,
    created_by_admin_user_id,
    used_by_supplier_user_id,
    used_at,
    created_at,
    updated_at
)
SELECT
    p.id,
    p.qr_code_id,
    CONCAT('q=', p.qr_code_id),
    'pages/qr-entry/index',
    CASE
        WHEN p.qr_code_url IS NULL OR p.qr_code_url = '' THEN NULL
        WHEN p.qr_code_url LIKE '/api/qrcodes/%' THEN p.qr_code_url
        ELSE NULL
    END,
    CASE
        WHEN p.fill_status = 'filled' THEN 'filled'
        WHEN p.fill_status = 'revoked' THEN 'revoked'
        ELSE 'unused'
    END,
    NULL,
    p.supplier_user_id,
    CASE WHEN p.fill_status = 'filled' THEN p.updated_at ELSE NULL END,
    p.created_at,
    p.updated_at
FROM prop_info p
WHERE p.qr_code_id IS NOT NULL
  AND p.qr_code_id != ''
  AND NOT EXISTS (
      SELECT 1 FROM prop_qr_code q WHERE q.qr_code_id = p.qr_code_id
  );
