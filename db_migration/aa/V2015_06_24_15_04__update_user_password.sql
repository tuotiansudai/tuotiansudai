BEGIN;
UPDATE `user` t SET t.`password` = SHA(CONCAT(t.`password`,t.`salt`));
COMMIT;