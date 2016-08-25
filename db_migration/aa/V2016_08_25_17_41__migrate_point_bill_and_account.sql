BEGIN ;

UPDATE point_bill pb
LEFT JOIN invest i ON pb.order_id = i.id
LEFT JOIN loan l ON i.loan_id = l.id
SET point = TRUNCATE(point*periods*30/365, 0)
WHERE pb.business_type = 'INVEST';

UPDATE account,
(SELECT SUM(point) AS sumpoint, login_name AS login_name FROM point_bill GROUP BY login_name) AS point_bill
SET account.point = IF(point_bill.sumpoint<0, 0, point_bill.sumpoint)
WHERE account.login_name = point_bill.login_name;

COMMIT ;