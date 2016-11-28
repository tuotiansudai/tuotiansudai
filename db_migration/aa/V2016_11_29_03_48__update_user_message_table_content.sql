BEGIN;

UPDATE `aa`.`user_message` SET content = title
WHERE content IS NULL;

COMMIT;
