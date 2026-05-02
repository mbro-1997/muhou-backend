-- Fix demo data generated before the prop audit workflow was tightened.
-- QR placeholder rows must not be visible or treated as approved inventory.
UPDATE `prop_info`
SET `audit_status` = 'pending',
    `prop_status` = 'offline'
WHERE `fill_status` = 'pending_fill';

-- Factory-filled rows with pending audit must stay offline until admin approval.
UPDATE `prop_info` p
JOIN `prop_audit` pa ON pa.`prop_id` = p.`id`
SET p.`audit_status` = 'pending',
    p.`prop_status` = 'offline'
WHERE p.`fill_status` = 'filled'
  AND pa.`audit_status` = 'pending';
