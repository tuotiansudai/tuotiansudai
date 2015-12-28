ALTER TABLE `aa`.`user_role` ADD INDEX INDEX_USER_ROLE_ROLE (`role`);

ALTER TABLE `aa`.`recharge` ADD INDEX INDEX_RECHARGE_CREATED_TIME_STATUS (`created_time`, `status`);

ALTER TABLE `aa`.`withdraw` ADD INDEX INDEX_WITHDRAW_CREATED_TIME (`created_time`);