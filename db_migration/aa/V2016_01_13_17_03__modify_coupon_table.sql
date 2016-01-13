ALTER TABLE coupon ADD COLUMN rate DOUBLE DEFAULT 0
AFTER amount;

ALTER TABLE coupon ADD COLUMN invest_upper_amount BIGINT UNSIGNED DEFAULT 0
AFTER invest_lower_limit;