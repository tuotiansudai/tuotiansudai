BEGIN ;

INSERT INTO coupon
VALUES
  (
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
    'admin',
    '2016-04-01 00:00:00',
    NULL,
    NULL,
    NULL,
    NULL,
    0,
    0,
    'WYX,JYF',
    'INTEREST_COUPON',
    122,
    'WINNER',
    0,
    0
  ) ;

INSERT INTO coupon
VALUES
  (
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
    'admin',
    '2016-04-01 00:00:00',
    NULL,
    NULL,
    NULL,
    NULL,
    0,
    0,
    'WYX,JYF',
    'INTEREST_COUPON',
    122,
    'WINNER',
    0,
    0
  ) ;

INSERT INTO coupon
VALUES
  (
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
    'admin',
    '2016-04-01 00:00:00',
    NULL,
    NULL,
    NULL,
    NULL,
    300000,
    0,
    'SYL',
    'INVEST_COUPON',
    122,
    'WINNER',
    0,
    0
  ) ;

COMMIT ;