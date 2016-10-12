BEGIN;

UPDATE coupon_repay coupon
        JOIN
    invest_repay invest ON coupon.invest_id = invest.invest_id  AND coupon.period = invest.period
SET
    coupon.repay_date = invest.repay_date
WHERE
    coupon.invest_id IN (SELECT
            invest_id
        FROM
            (SELECT
                invest_id
            FROM
                coupon_repay
            GROUP BY invest_id , coupon_id , repay_date
            HAVING COUNT(1) > 1) coupon);

COMMIT;