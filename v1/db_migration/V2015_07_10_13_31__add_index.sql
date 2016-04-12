ALTER TABLE `recharge` ADD INDEX IDX_STATUS_USER_ID (`status`, `user_id`);
ALTER TABLE `user` MODIFY `referrer` VARCHAR(32);
ALTER TABLE `user` ADD INDEX IDX_REFERRER (`referrer`);