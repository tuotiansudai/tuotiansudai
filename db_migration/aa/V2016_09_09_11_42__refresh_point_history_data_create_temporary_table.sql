BEGIN;
USE aa;

CREATE TABLE point_bill_temp
  LIKE point_bill;

CREATE TABLE user_point_task_temp
  LIKE user_point_task;

CREATE TABLE tmp (
  login_name VARCHAR(25) PRIMARY KEY,
  point      INT
);

COMMIT;