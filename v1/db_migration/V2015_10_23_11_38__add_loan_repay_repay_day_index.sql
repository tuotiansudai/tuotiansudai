

BEGIN ;

ALTER TABLE `loan_repay` ADD INDEX INDEX_LOAN_REPAY_REPAY_DAY (`repay_day`);

COMMIT ;






