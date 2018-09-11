ALTER TABLE `aa`.`user`
  ADD COLUMN `ump_user_name`        VARCHAR(50);

ALTER TABLE `aa`.`user`
  ADD COLUMN `ump_identity_number`  VARCHAR(20);

UPDATE `user` SET `ump_user_name` = `user_name`,`ump_identity_number`=`identity_number`,`identity_number`=NULL ,`user_name`=NULL;

