BEGIN;

UPDATE user_coupon
SET coupon_id = 382
WHERE coupon_id = 100035 AND now() BETWEEN `start_time` AND `end_time` AND (status != 'SUCCESS' OR status IS NULL);

UPDATE coupon
SET issued_count = (SELECT count(1)
                    FROM user_coupon
                    WHERE coupon_id = 382)
WHERE id = 382;

UPDATE coupon
SET issued_count = (SELECT count(1)
                    FROM user_coupon
                    WHERE coupon_id = 100035)
WHERE id = 100035;

INSERT INTO user_coupon (login_name, coupon_id, start_time, end_time, created_time)
  SELECT temp.login_name, 383, temp.start_time, temp.end_time, temp.created_time
  FROM (SELECT login_name, start_time, end_time, created_time
        FROM user_coupon
        WHERE coupon_id = 382) temp;

INSERT INTO user_coupon (login_name, coupon_id, start_time, end_time, created_time)
  SELECT temp.login_name, 384, temp.start_time, temp.end_time, temp.created_time
  FROM (SELECT login_name, start_time, end_time, created_time
        FROM user_coupon
        WHERE coupon_id = 382) temp;

INSERT INTO user_coupon (login_name, coupon_id, start_time, end_time, created_time)
  SELECT temp.login_name, 385, temp.start_time, temp.end_time, temp.created_time
  FROM (SELECT login_name, start_time, end_time, created_time
        FROM user_coupon
        WHERE coupon_id = 382) temp;

INSERT INTO user_coupon (login_name, coupon_id, start_time, end_time, created_time)
  SELECT temp.login_name, 386, temp.start_time, temp.end_time, temp.created_time
  FROM (SELECT login_name, start_time, end_time, created_time
        FROM user_coupon
        WHERE coupon_id = 382) temp;

INSERT INTO user_coupon (login_name, coupon_id, start_time, end_time, created_time)
  SELECT temp.login_name, 387, temp.start_time, temp.end_time, temp.created_time
  FROM (SELECT login_name, start_time, end_time, created_time
        FROM user_coupon
        WHERE coupon_id = 382) temp;

INSERT INTO user_coupon (login_name, coupon_id, start_time, end_time, created_time)
  SELECT temp.login_name, 388, temp.start_time, temp.end_time, temp.created_time
  FROM (SELECT login_name, start_time, end_time, created_time
        FROM user_coupon
        WHERE coupon_id = 382) temp;

INSERT INTO user_coupon (login_name, coupon_id, start_time, end_time, created_time)
  SELECT temp.login_name, 389, temp.start_time, temp.end_time, temp.created_time
  FROM (SELECT login_name, start_time, end_time, created_time
        FROM user_coupon
        WHERE coupon_id = 382) temp;

INSERT INTO user_coupon (login_name, coupon_id, start_time, end_time, created_time)
  SELECT temp.login_name, 390, temp.start_time, temp.end_time, temp.created_time
  FROM (SELECT login_name, start_time, end_time, created_time
        FROM user_coupon
        WHERE coupon_id = 382) temp;

COMMIT;