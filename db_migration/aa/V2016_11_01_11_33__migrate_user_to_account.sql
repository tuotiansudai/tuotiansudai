BEGIN;

UPDATE `user`
SET
  `user`.`user_name`       = (SELECT `user_name`
                              FROM `account`
                              WHERE `user`.`login_name` = `account`.`login_name`),
  `user`.`identity_number` = (SELECT `identity_number`
                              FROM `account`
                              WHERE `user`.`login_name` = `account`.`login_name`);

COMMIT;
