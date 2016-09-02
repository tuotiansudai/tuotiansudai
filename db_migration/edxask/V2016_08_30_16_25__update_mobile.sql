BEGIN;

UPDATE `question`
SET `mobile` = (SELECT mobile
                FROM `aa`.`user`
                WHERE `aa`.`user`.`login_name` = `question`.login_name);

UPDATE `answer`
SET `mobile` = (SELECT mobile
                FROM `aa`.`user`
                WHERE `aa`.`user`.`login_name` = `answer`.login_name);

COMMIT;