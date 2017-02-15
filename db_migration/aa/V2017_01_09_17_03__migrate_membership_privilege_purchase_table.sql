BEGIN;
INSERT INTO membership_privilege_purchase (login_name, mobile, user_name, privilege, privilege_price_type, amount, source, status, created_time)
  SELECT
    login_name,
    mobile,
    user_name,
    'SERVICE_FEE',
    CASE duration
    WHEN 30
      THEN '_30'
    WHEN 180
      THEN '_180'
    WHEN 360
      THEN '_360' END AS 'privilege_price_type',
    amount,
    source,
    status,
    created_time
  FROM membership_purchase
  WHERE status = 'SUCCESS';
COMMIT;