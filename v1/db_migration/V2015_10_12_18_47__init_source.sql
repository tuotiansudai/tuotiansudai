BEGIN ;
UPDATE `user` set `source` = 'WEB';
UPDATE `recharge` set `source` = 'WEB';
UPDATE `withdraw_cash` set `source` = 'WEB';
UPDATE `invest` set `source` = 'WEB';
COMMIT ;