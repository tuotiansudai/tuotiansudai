BEGIN;

UPDATE coupon_repay coupon join invest_repay invest on coupon.invest_id = invest.invest_id and coupon.period = invest.period
SET
    coupon.status = invest.status
WHERE
	coupon.status = 'OVERDUE';

COMMIT;