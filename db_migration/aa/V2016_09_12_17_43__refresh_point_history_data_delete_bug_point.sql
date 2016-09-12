BEGIN;
-- create tmp table
CREATE TABLE tmp (
  login_name VARCHAR(25) PRIMARY KEY,
  point      INT
);

-- update
INSERT INTO tmp SELECT
                  account.login_name,
                  account.point - (SELECT SUM(point)
                                   FROM point_bill
                                   WHERE point = 1000 AND business_type = 'TASK')
                FROM point_bill
                  JOIN account
                    ON account.login_name = point_bill.login_name AND point_bill.point = 1000 AND business_type = 'TASK'
                GROUP BY login_name;

DELETE FROM point_bill
WHERE point = 1000 AND business_type = 'TASK';
DELETE FROM user_point_task
WHERE point = 1000 AND point_task_id = (SELECT id
                                        FROM point_task
                                        WHERE name = 'EACH_REFERRER_INVEST');

UPDATE `aa`.`account`, tmp
SET account.point = tmp.point
WHERE tmp.login_name = account.login_name;

DROP TABLE tmp;

COMMIT;