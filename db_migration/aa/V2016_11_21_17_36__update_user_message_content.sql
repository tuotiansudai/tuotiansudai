BEGIN;

UPDATE `aa`.`user_message`
SET content = title;

COMMIT;