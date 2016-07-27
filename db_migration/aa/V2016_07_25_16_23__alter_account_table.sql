BEGIN;
ALTER TABLE `aa`.`account`
ADD UNIQUE INDEX `login_name_UNIQUE` (`login_name` ASC);
COMMIT;