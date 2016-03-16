BEGIN;
UPDATE user SET last_modified_time = register_time WHERE last_modified_time IS NULL;
COMMIT;