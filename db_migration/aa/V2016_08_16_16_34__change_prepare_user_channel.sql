BEGIN ;
ALTER TABLE `aa`.`prepare_user`
CHANGE COLUMN `channel` `channel` VARCHAR(32) NULL ;

COMMIT ;