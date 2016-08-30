BEGIN ;

UPDATE point_bill pb
LEFT JOIN invest i ON pb.order_id = i.id
LEFT JOIN loan l ON i.loan_id = l.id
SET point = TRUNCATE(point*duration/365, 0)
WHERE pb.business_type = 'INVEST';

UPDATE account
 SET point = (SELECT IF(SUM(point)<0, 0, SUM(point)) FROM point_bill WHERE point_bill.login_name = account.login_name);

COMMIT ;