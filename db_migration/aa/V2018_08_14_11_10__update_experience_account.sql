BEGIN;

UPDATE experience_account as ec,
    (SELECT i.login_name ,SUM(i.amount) AS balance FROM  invest_repay ir  JOIN invest i ON ir.invest_id = i.id AND i.loan_id = 1 WHERE ir.status='REPAYING' GROUP BY i.login_name) AS c
SET
    ec.experience_balance=ec.experience_balance+c.balance
WHERE
    ec.login_name=c.login_name;

UPDATE  invest
SET
  status='FAIL'
WHERE
  id  IN (select s1.invest_id from (SELECT ir.invest_id FROM  invest_repay ir  JOIN invest i ON ir.invest_id = i.id AND i.loan_id = 1 WHERE ir.status='REPAYING') as s1);

DELETE FROM   invest_repay 
WHERE
   id IN (select s.id from (SELECT ir.id FROM  invest_repay ir  JOIN invest i ON ir.invest_id = i.id AND i.loan_id = 1 WHERE ir.status='REPAYING') as s);

COMMIT;