BEGIN ;

insert ignore into user (login_name,password,mobile,register_time,status,salt) values ('sidneygao','e8ba3a39cef651c08fbd7f8df591760f6b7412a4','13810586920',now(),'ACTIVE','083e54eaef1f42afaec76a077f571693');

INSERT INTO coupon
SELECT
    300,
    0,
    0.005,
    0,
    1,
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
    1,
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
    1,
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