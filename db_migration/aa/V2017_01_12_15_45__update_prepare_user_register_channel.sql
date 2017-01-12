BEGIN;

UPDATE `aa`.`prepare_user` SET `register_channel` = 'APP_SHARE' where `register_channel` is null or `register_channel` = '';

COMMIT;