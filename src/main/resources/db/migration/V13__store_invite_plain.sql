ALTER TABLE factory_invite_code
    ADD COLUMN code_plain VARCHAR(64) NULL COMMENT '邀请码明文，演示阶段用于后续查看，生产环境建议加密存储' AFTER code_hash;
