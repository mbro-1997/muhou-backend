UPDATE `admin_account`
SET `password_hash` = '$2a$10$a5dmxTBwN74rC88c9bc31.7dFSDNMvswBeE.fRlRj4yIto4tMcs0e',
    `updated_at` = NOW()
WHERE `username` = 'admin';
