BEGIN;

UPDATE message SET push_id = (select id from jpush_alert where message_id = message.id);

COMMIT;