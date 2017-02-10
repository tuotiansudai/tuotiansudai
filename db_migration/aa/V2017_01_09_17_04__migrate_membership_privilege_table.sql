BEGIN;
INSERT INTO membership_privilege (login_name, privilege, start_time, end_time, created_time)
  SELECT
    login_name,
    'SERVICE_FEE',
    created_time            AS 'start_time',
    DATE_ADD(
        DATE_ADD(str_to_date(DATE_FORMAT(created_time, '%Y-%m-%d'), '%Y-%m-%d %H:%i:%s'), INTERVAL duration + 1 DAY),
        INTERVAL -1 SECOND) AS 'end_time',
    created_time
  FROM membership_purchase
  WHERE status = 'SUCCESS';
COMMIT;