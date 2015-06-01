CREATE TABLE `loan_his` AS SELECT * FROM `loan`;
UPDATE `loan` SET `status` = 'test';

CREATE TABLE loan_repay_his AS SELECT * FROM loan_repay;
UPDATE `loan_repay` SET `status` = 'test';

CREATE TABLE `invest_his` AS SELECT * FROM `invest`;
UPDATE `invest` SET `status` = 'test';

CREATE TABLE `invest_repay_his` AS SELECT * FROM `invest_repay`;
UPDATE `invest_repay` SET `status` = 'test';

CREATE TABLE `user_bill_his` AS SELECT * FROM `user_bill`;
TRUNCATE TABLE `user_bill`;

CREATE TABLE `system_bill_his` AS SELECT * FROM `system_bill`;
TRUNCATE TABLE `system_bill`;

CREATE TABLE `recharge_his` AS SELECT * FROM `recharge`;
TRUNCATE TABLE `recharge`;

CREATE TABLE `withdraw_cash_his` AS SELECT * FROM `withdraw_cash`;
TRUNCATE TABLE `withdraw_cash`;