BEGIN;
UPDATE user_bill
  INNER JOIN (SELECT u.id
              FROM user_bill u
                JOIN transfer_application t ON u.order_id = t.id
              WHERE u.business_type = 'INVEST_FEE' AND t.status = 'SUCCESS' AND
                    t.transfer_time BETWEEN '2018-12-17 00:00:00' AND '2018-12-17 23:00:00') temp ON user_bill.id = temp.id
SET business_type = 'TRANSFER_INVEST_FEE';
COMMIT;