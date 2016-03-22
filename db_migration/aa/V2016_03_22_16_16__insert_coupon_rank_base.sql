BEGIN ;

INSERT INTO coupon
SELECT
    300,
    0,
    0.005,
    0,
    0,
    '2016-04-01 00:00:00',
    '2016-07-31 23:59:59',
    0,
    2147483647,
    0,
    TRUE,
    FALSE,
    'sidneygao',
    '2016-04-01 00:00:00',
    NULL,
    NULL,
    NULL,
    NULL,
    0,
    0,
    'WYX,JYF',
    'INTEREST_COUPON',
    0,
    'WINNER',
    0,
    0
  FROM dual WHERE EXISTS (SELECT * from `user` where login_name = 'sidneygao');

INSERT INTO coupon
SELECT
    301,
    0,
    0.002,
    0,
    0,
    '2016-04-01 00:00:00',
    '2016-07-31 23:59:59',
    0,
    2147483647,
    0,
    TRUE,
    FALSE,
    'sidneygao',
    '2016-04-01 00:00:00',
    NULL,
    NULL,
    NULL,
    NULL,
    0,
    0,
    'WYX,JYF',
    'INTEREST_COUPON',
    0,
    'WINNER',
    0,
    0
  FROM dual WHERE EXISTS (SELECT * from `user` where login_name = 'sidneygao') ;

INSERT INTO coupon
SELECT
    302,
    300000,
    0,
    0,
    0,
    '2016-04-01 00:00:00',
    '2016-07-31 23:59:59',
    0,
    2147483647,
    0,
    TRUE,
    FALSE,
    'sidneygao',
    '2016-04-01 00:00:00',
    NULL,
    NULL,
    NULL,
    NULL,
    300000,
    0,
    'SYL',
    'INVEST_COUPON',
    0,
    'WINNER',
    0,
    0
  FROM dual WHERE EXISTS (SELECT * from `user` where login_name = 'sidneygao') ;

COMMIT ;