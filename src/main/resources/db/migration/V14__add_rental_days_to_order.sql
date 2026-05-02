ALTER TABLE rental_order
    ADD COLUMN rental_days INT NOT NULL DEFAULT 1 COMMENT '租赁天数' AFTER order_status;
