BEGIN ;
ALTER TABLE `aa`.`user_coupon`
ADD COLUMN `achievement_loan_id` BIGINT(20) UNSIGNED NULL DEFAULT NULL AFTER `exchange_code`;
COMMIT ;