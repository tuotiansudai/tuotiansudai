BEGIN;

UPDATE account SET user_name = trim(user_name);

COMMIT;
