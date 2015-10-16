BEGIN ;
UPDATE `system_bill` SET `reason` = 'invest_fee' WHERE `reason` IN ('normal_repay','overdue_repay') AND `detail` LIKE '%收到还款，扣除手续费, 还款ID%';
UPDATE `user_bill` SET `type_info` = 'invest_fee' WHERE `type_info` IN ('normal_repay','overdue_repay') AND `detail` LIKE '%收到还款，扣除手续费, 还款ID%';
COMMIT ;