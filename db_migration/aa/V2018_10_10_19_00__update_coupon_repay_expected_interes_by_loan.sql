BEGIN;

UPDATE coupon_repay
  JOIN (SELECT
          cr.id,
          cr.expected_interest,
          cr.expected_fee,
          to_days(l.deadline) - to_days(l.recheck_time) + 1 AS period,
          i.amount                                          AS amount,
          i.invest_fee_rate                                 AS fee_rate,
          c.rate                                            AS rate
        FROM coupon_repay cr
          JOIN invest i ON cr.invest_id = i.id
          JOIN loan l ON i.loan_id = l.id
          JOIN coupon c ON c.id = cr.coupon_id
        WHERE l.id IN ('112963360205872')) temp ON coupon_repay.id = temp.id
SET coupon_repay.expected_interest = floor(temp.amount * temp.rate / 365 * temp.period),
  coupon_repay.expected_fee        = floor(temp.amount * temp.rate / 365 * temp.period * temp.fee_rate);

UPDATE user_coupon
  JOIN (SELECT
          uc.id,
          uc.expected_interest,
          uc.expected_fee,
          to_days(l.deadline) - to_days(l.recheck_time) + 1 AS period,
          i.amount,
          i.invest_fee_rate                                 AS fee_rate,
          c.rate                                            AS rate
        FROM user_coupon uc
          JOIN invest i ON uc.invest_id = i.id
          JOIN loan l ON i.loan_id = l.id
          JOIN coupon c ON c.id = uc.coupon_id
        WHERE l.id IN ('30497451731632')) temp ON user_coupon.id = temp.id
SET user_coupon.expected_interest = floor(temp.amount * temp.rate / 365 * temp.period),
  user_coupon.expected_fee        = floor(temp.amount * temp.rate / 365 * temp.period * temp.fee_rate);

COMMIT;