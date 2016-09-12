DELETE FROM point_bill
WHERE point = 1000 AND business_type = 'TASK';
DELETE FROM user_point_task
WHERE point = 1000 AND point_task_id = (SELECT id
                                        FROM point_task
                                        WHERE name = 'EACH_REFERRER_INVEST');

-- create tmp table
CREATE TABLE tmp (
  login_name VARCHAR(25) PRIMARY KEY,
  point      INT
);
-- default point = 0
UPDATE `aa`.`account`
SET point = 0;
-- update
INSERT INTO tmp SELECT
                  login_name,
                  SUM(point)
                FROM point_bill
                GROUP BY login_name;

-- SUM result may negative
UPDATE tmp
SET tmp.point = 0
WHERE tmp.point < 0;

UPDATE `aa`.`account`, tmp
SET account.point = tmp.point
WHERE tmp.login_name = account.login_name;

DROP TABLE tmp;