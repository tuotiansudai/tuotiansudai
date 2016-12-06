BEGIN;

ALTER TABLE jpush_alert DROP COLUMN `message_id`;

COMMIT;