CREATE INDEX `idx_rental_order_status_returned_at`
  ON `rental_order` (`order_status`, `returned_at`);
