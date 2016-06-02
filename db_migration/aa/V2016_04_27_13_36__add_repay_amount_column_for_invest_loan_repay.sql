ALTER TABLE loan_repay ADD COLUMN repay_amount BIGINT UNSIGNED DEFAULT 0
AFTER default_interest;

ALTER TABLE invest_repay ADD COLUMN repay_amount BIGINT UNSIGNED DEFAULT 0
AFTER actual_fee;